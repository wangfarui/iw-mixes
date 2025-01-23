package com.itwray.iw.points.job;

import cn.hutool.json.JSONUtil;
import com.itwray.iw.points.dao.PointsTaskBaseDao;
import com.itwray.iw.points.dao.PointsTaskPeriodicDao;
import com.itwray.iw.points.model.bo.PointsTaskFullBo;
import com.itwray.iw.points.model.dto.PointsTaskConditionDto;
import com.itwray.iw.points.model.enums.PointsTaskPeriodicIntervalEnum;
import com.itwray.iw.points.model.enums.PointsTaskPeriodicTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户定时任务
 *
 * @author wray
 * @since 2024/12/27
 */
@Component
@Slf4j
public class PointsTaskScheduledJob {

    private final PointsTaskBaseDao pointsTaskBaseDao;

    private final PointsTaskPeriodicDao pointsTaskPeriodicDao;

    public PointsTaskScheduledJob(PointsTaskBaseDao pointsTaskBaseDao, PointsTaskPeriodicDao pointsTaskPeriodicDao) {
        this.pointsTaskBaseDao = pointsTaskBaseDao;
        this.pointsTaskPeriodicDao = pointsTaskPeriodicDao;
    }

    /**
     * 处理每日任务执行情况
     * <p>每天0点0分0秒</p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void handleDailyTask() {
        // 查询周期性的打卡任务
        PointsTaskConditionDto conditionDto = new PointsTaskConditionDto();
        conditionDto.setPeriodicType(PointsTaskPeriodicTypeEnum.CLOCK_IN);
        conditionDto.setPeriodicInterval(PointsTaskPeriodicIntervalEnum.DAILY);
        List<PointsTaskFullBo> taskFullBos = pointsTaskPeriodicDao.getBaseMapper().queryClockInTask(conditionDto);
        log.info("周期性任务：{}", JSONUtil.toJsonStr(taskFullBos));
    }
}
