package com.itwray.iw.points.job;

import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.points.dao.PointsTaskBasicsDao;
import com.itwray.iw.points.dao.PointsTaskPlanDao;
import com.itwray.iw.points.dao.PointsTaskRelationDao;
import com.itwray.iw.points.model.bo.ExpiredTaskBo;
import com.itwray.iw.points.model.dto.PointsRecordsAddDto;
import com.itwray.iw.points.model.dto.task.PointsTaskRelationAddDto;
import com.itwray.iw.points.model.dto.task.TaskBasicsAddDto;
import com.itwray.iw.points.model.entity.PointsTaskBasicsEntity;
import com.itwray.iw.points.model.entity.PointsTaskPlanEntity;
import com.itwray.iw.points.model.entity.PointsTaskRelationEntity;
import com.itwray.iw.points.model.enums.PointsSourceTypeEnum;
import com.itwray.iw.points.model.enums.PointsTransactionTypeEnum;
import com.itwray.iw.points.model.enums.TaskStatusEnum;
import com.itwray.iw.points.model.param.QueryExpiredTaskParam;
import com.itwray.iw.points.model.param.QueryPlanTaskParam;
import com.itwray.iw.points.service.PointsTaskBasicsService;
import com.itwray.iw.points.service.PointsTaskRelationService;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.model.enums.mq.PointsRecordsTopicEnum;
import com.itwray.iw.web.utils.ApplicationContextHolder;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 积分任务的定时任务
 *
 * @author wray
 * @since 2025/4/21
 */
@Component
@Slf4j
public class PointsTaskScheduledJob {

    private final PointsTaskBasicsDao pointsTaskBasicsDao;

    private final PointsTaskRelationDao pointsTaskRelationDao;

    private final PointsTaskPlanDao pointsTaskPlanDao;

    private TransactionTemplate transactionTemplate;

    @Autowired
    public PointsTaskScheduledJob(PointsTaskBasicsDao pointsTaskBasicsDao, PointsTaskRelationDao pointsTaskRelationDao,
                                  PointsTaskPlanDao pointsTaskPlanDao) {
        this.pointsTaskBasicsDao = pointsTaskBasicsDao;
        this.pointsTaskRelationDao = pointsTaskRelationDao;
        this.pointsTaskPlanDao = pointsTaskPlanDao;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * 每天凌晨0点执行, 处理已过期未执行的任务
     * TODO 前提条件：points服务是单实例
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void handleExpiredTask() {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("Job[handleExpiredTask]任务开始执行, 开始时间: {}", LocalDateTime.now());

        QueryExpiredTaskParam param = new QueryExpiredTaskParam();
        param.setTaskStatus(TaskStatusEnum.WAIT.getCode());
        param.setPunishStatus(BoolEnum.FALSE.getCode());
        param.setDeadlineDate(LocalDate.now());
        while (true) {
            List<ExpiredTaskBo> taskList = pointsTaskBasicsDao.getBaseMapper().queryExpiredTask(param);
            if (taskList.isEmpty()) {
                break;
            }
            for (ExpiredTaskBo bo : taskList) {
                transactionTemplate.execute(action -> {
                    pointsTaskRelationDao.lambdaUpdate()
                            .eq(PointsTaskRelationEntity::getId, bo.getRelationId())
                            .set(PointsTaskRelationEntity::getPunishStatus, BoolEnum.TRUE.getCode())
                            .update();

                    log.info("未完成任务[" + bo.getTaskId() + "]" + bo.getTaskName());

                    if (bo.getPunishPoints() != 0) {
                        PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
                        pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.DEDUCT.getCode());
                        pointsRecordsAddDto.setPoints(-bo.getPunishPoints());
                        pointsRecordsAddDto.setSource("未完成任务[" + bo.getTaskId() + "]" + bo.getTaskName());
                        pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.POINTS_TASK_TIMING.getCode());
                        pointsRecordsAddDto.setUserId(bo.getUserId());
                        MQProducerHelper.send(PointsRecordsTopicEnum.TASK, pointsRecordsAddDto);
                    }

                    return true;
                });
            }
        }

        log.info("Job[handleExpiredTask]任务执行完毕, 执行用时: {}s", (System.currentTimeMillis() - currentTimeMillis) / 1000);
    }

    /**
     * 每天凌晨1点执行, 根据任务计划生成任务
     * TODO 前提条件：points服务是单实例
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void handlePlanTask() {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("Job[handlePlanTask]任务开始执行, 开始时间: {}", LocalDateTime.now());
        PointsTaskBasicsService taskBasicsService = ApplicationContextHolder.getBean(PointsTaskBasicsService.class);
        PointsTaskRelationService taskRelationService = ApplicationContextHolder.getBean(PointsTaskRelationService.class);

        QueryPlanTaskParam param = new QueryPlanTaskParam();
        param.setNextPlanDate(LocalDate.now());
        param.setStatus(BoolEnum.TRUE.getCode());
        param.setPageSize(100L);
        while (true) {
            List<PointsTaskPlanEntity> taskPlanEntityList = pointsTaskPlanDao.getBaseMapper().queryPlanTask(param);
            if (taskPlanEntityList.isEmpty()) {
                break;
            }
            for (PointsTaskPlanEntity taskPlanEntity : taskPlanEntityList) {
                try {
                    UserUtils.setUserId(taskPlanEntity.getUserId());
                    transactionTemplate.execute(action -> {
                        // 计算下一次的计划日期
                        LocalDate planDate = this.computePlanDate(taskPlanEntity);

                        // 更新计划任务的计划日期和下次计划生成日期
                        pointsTaskPlanDao.lambdaUpdate()
                                .eq(PointsTaskPlanEntity::getId, taskPlanEntity.getId())
                                .set(PointsTaskPlanEntity::getPlanDate, planDate)
                                // 下一次的计划生成日期 = 下一次的计划日期 - 提前提醒天数
                                .set(PointsTaskPlanEntity::getNextPlanDate, planDate.minusDays(taskPlanEntity.getRemindDays()))
                                .update();

                        // 新增任务基础实体
                        TaskBasicsAddDto addDto = new TaskBasicsAddDto();
                        addDto.setTaskGroupId(WebCommonConstants.DATABASE_DEFAULT_INT_VALUE);
                        addDto.setTaskName(taskPlanEntity.getTaskName());
                        addDto.setTaskRemark(taskPlanEntity.getTaskRemark());
                        addDto.setDeadlineDate(taskPlanEntity.getPlanDate().plusDays(taskPlanEntity.getDeadlineDays()));
                        Integer taskId = taskBasicsService.add(addDto);

                        // 新增任务关联实体
                        if (taskPlanEntity.getPunishPoints() > 0 || taskPlanEntity.getRewardPoints() > 0) {
                            PointsTaskRelationAddDto taskRelationAddDto = new PointsTaskRelationAddDto();
                            taskRelationAddDto.setTaskId(taskId);
                            taskRelationAddDto.setRewardPoints(taskPlanEntity.getRewardPoints());
                            taskRelationAddDto.setPunishPoints(taskPlanEntity.getPunishPoints());
                            taskRelationService.add(taskRelationAddDto);
                        }

                        return true;
                    });
                } finally {
                    UserUtils.removeUserId();
                }
            }
        }

        log.info("Job[handlePlanTask]任务执行完毕, 执行用时: {}s", (System.currentTimeMillis() - currentTimeMillis) / 1000);
    }

    private void syncPoints(PointsTaskBasicsEntity taskBasicsEntity, Integer points, boolean isFinish) {
        if (points == null || points == 0) {
            return;
        }
        PointsRecordsAddDto pointsRecordsAddDto = new PointsRecordsAddDto();
        pointsRecordsAddDto.setTransactionType(PointsTransactionTypeEnum.DEDUCT.getCode());
        pointsRecordsAddDto.setPoints(isFinish ? points : -points);
        pointsRecordsAddDto.setSource((isFinish ? "完成任务" : "取消任务") + "[" + taskBasicsEntity.getId() + "]" + taskBasicsEntity.getTaskName());
        pointsRecordsAddDto.setSourceType(PointsSourceTypeEnum.POINTS_TASK_MANUAL.getCode());
        pointsRecordsAddDto.setUserId(taskBasicsEntity.getUserId());
        MQProducerHelper.send(PointsRecordsTopicEnum.TASK, pointsRecordsAddDto);
    }

    private LocalDate computePlanDate(PointsTaskPlanEntity entity) {
        LocalDate planDate = entity.getPlanDate();
        switch (entity.getPlanCycle()) {
            case DAY -> planDate = planDate.plusDays(1);
            case WEEK -> planDate = planDate.plusWeeks(1);
            case MONTH -> planDate = planDate.plusMonths(1);
            case YEAR -> planDate = planDate.plusYears(1);
            case CUSTOM -> planDate = planDate.plusDays(entity.getCycleDays());
        }
        return planDate;
    }
}
