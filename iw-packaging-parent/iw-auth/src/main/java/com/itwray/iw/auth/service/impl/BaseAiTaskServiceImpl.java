package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.auth.dao.BaseAiTaskDao;
import com.itwray.iw.auth.mapper.BaseAiTaskMapper;
import com.itwray.iw.auth.model.dto.AiTaskAddDto;
import com.itwray.iw.auth.model.dto.AiTaskPageDto;
import com.itwray.iw.auth.model.dto.AiTaskUpdateDto;
import com.itwray.iw.auth.model.entity.BaseAiTaskEntity;
import com.itwray.iw.auth.model.vo.AiTaskDetailVo;
import com.itwray.iw.auth.model.vo.AiTaskPageVo;
import com.itwray.iw.auth.service.BaseAiTaskService;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * AI任务服务实现类
 *
 * @author wray
 * @since 2026-03-26
 */
@Service
public class BaseAiTaskServiceImpl extends WebServiceImpl<BaseAiTaskDao, BaseAiTaskMapper, BaseAiTaskEntity,
        AiTaskAddDto, AiTaskUpdateDto, AiTaskDetailVo, Integer> implements BaseAiTaskService {

    public BaseAiTaskServiceImpl(BaseAiTaskDao baseDao) {
        super(baseDao);
    }

    @Override
    @Transactional
    public Integer add(AiTaskAddDto dto) {
        BaseAiTaskEntity entity = BeanUtil.copyProperties(dto, BaseAiTaskEntity.class);
        entity.setLastActiveAt(LocalDateTime.now());
        getBaseDao().save(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public void update(AiTaskUpdateDto dto) {
        getBaseDao().queryById(dto.getId());
        BaseAiTaskEntity entity = BeanUtil.copyProperties(dto, BaseAiTaskEntity.class);
        entity.setLastActiveAt(LocalDateTime.now());
        getBaseDao().updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        getBaseDao().queryById(id);
        getBaseDao().removeById(id);
    }

    @Override
    public AiTaskDetailVo detail(Integer id) {
        BaseAiTaskEntity entity = getBaseDao().queryById(id);
        return BeanUtil.copyProperties(entity, AiTaskDetailVo.class);
    }

    @Override
    public PageVo<AiTaskPageVo> page(AiTaskPageDto dto) {
        LambdaQueryWrapper<BaseAiTaskEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dto.getTaskStatus() != null, BaseAiTaskEntity::getTaskStatus, dto.getTaskStatus())
                .eq(dto.getToolType() != null, BaseAiTaskEntity::getToolType, dto.getToolType())
                .like(StringUtils.isNotBlank(dto.getProjectName()), BaseAiTaskEntity::getProjectName, dto.getProjectName())
                .eq(StringUtils.isNotBlank(dto.getSessionKey()), BaseAiTaskEntity::getSessionKey, dto.getSessionKey())
                .like(StringUtils.isNotBlank(dto.getWorkspaceKeyword()), BaseAiTaskEntity::getWorkspacePath, dto.getWorkspaceKeyword())
                .orderByDesc(BaseAiTaskEntity::getLastActiveAt)
                .orderByDesc(BaseAiTaskEntity::getId);
        if (StringUtils.isNotBlank(dto.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper.like(BaseAiTaskEntity::getTitle, dto.getKeyword())
                    .or().like(BaseAiTaskEntity::getDescription, dto.getKeyword())
                    .or().like(BaseAiTaskEntity::getSessionKey, dto.getKeyword())
                    .or().like(BaseAiTaskEntity::getProjectName, dto.getKeyword())
                    .or().like(BaseAiTaskEntity::getWorkspacePath, dto.getKeyword()));
        }

        return getBaseDao().page(dto, queryWrapper, entity -> BeanUtil.copyProperties(entity, AiTaskPageVo.class));
    }
}
