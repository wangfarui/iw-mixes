package com.itwray.iw.points.dao;

import com.itwray.iw.points.mapper.PointsTaskBaseMapper;
import com.itwray.iw.points.model.bo.PointsTaskFullBo;
import com.itwray.iw.points.model.entity.PointsTaskBaseEntity;
import com.itwray.iw.web.dao.BaseDao;
import com.itwray.iw.web.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 * 任务基础表 DAO
 *
 * @author wray
 * @since 2025-01-13
 */
@Component
public class PointsTaskBaseDao extends BaseDao<PointsTaskBaseMapper, PointsTaskBaseEntity> {

    /**
     * 查询任务实体完整对象
     *
     * @param id 任务id
     * @return PointsTaskFullBo
     */
    public PointsTaskFullBo queryOneById(Integer id) {
        PointsTaskFullBo bo = getBaseMapper().queryOneById(id);
        if (bo == null) {
            throw new BusinessException("任务不存在");
        }
        return bo;
    }
}
