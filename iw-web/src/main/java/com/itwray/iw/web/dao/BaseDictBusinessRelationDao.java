package com.itwray.iw.web.dao;

import cn.hutool.core.collection.CollUtil;
import com.itwray.iw.web.mapper.BaseDictBusinessRelationMapper;
import com.itwray.iw.web.model.entity.BaseDictBusinessRelationEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典业务关联表 DAO
 *
 * @author wray
 * @since 2024/9/13
 */
@Component
public class BaseDictBusinessRelationDao extends BaseDao<BaseDictBusinessRelationMapper, BaseDictBusinessRelationEntity> {

    /**
     * 保存业务表与字典集合的关联关系
     *
     * @param businessId 业务表id
     * @param dictIdList 字典id集合
     */
    @Transactional
    public void saveRelation(Integer businessId, List<Integer> dictIdList) {
        // 删除之前的关联关系
        this.removeRelation(businessId);

        // 保存新的关联关系
        if (CollUtil.isNotEmpty(dictIdList)) {
            List<BaseDictBusinessRelationEntity> relationEntities = dictIdList.stream()
                    .map(dictId -> new BaseDictBusinessRelationEntity(businessId, dictId))
                    .collect(Collectors.toList());
            this.saveBatch(relationEntities);
        }
    }

    /**
     * 删除业务表与字典的关联关系
     *
     * @param businessId 业务表id
     */
    @Transactional
    public void removeRelation(Serializable businessId) {
        this.lambdaUpdate().eq(BaseDictBusinessRelationEntity::getId, businessId).remove();
    }

    /**
     * 查询字典id集合
     *
     * @param businessId 业务表id
     * @return 字典id集合
     */
    public List<Integer> queryDictIdList(Serializable businessId) {
        return this.lambdaQuery().eq(BaseDictBusinessRelationEntity::getId, businessId)
                .list()
                .stream()
                .map(BaseDictBusinessRelationEntity::getDictId)
                .collect(Collectors.toList());
    }
}
