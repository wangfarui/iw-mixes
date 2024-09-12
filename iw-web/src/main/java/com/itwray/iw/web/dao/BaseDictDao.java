package com.itwray.iw.web.dao;

import com.itwray.iw.common.constants.EnableEnums;
import com.itwray.iw.web.constants.WebCommonConstants;
import com.itwray.iw.web.mapper.BaseDictMapper;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import org.springframework.stereotype.Component;

/**
 * 字典表 DAO
 *
 * @author wray
 * @since 2024/5/26
 */
@Component
public class BaseDictDao extends BaseDao<BaseDictMapper, BaseDictEntity> {

    /**
     * 查询排序最大值的下一个值
     *
     * @param dictType 字典类型
     * @return 排序最大值 + 1
     */
    public Integer queryNextSortValue(Integer dictType) {
        BaseDictEntity baseDictEntity = this.lambdaQuery()
                .eq(BaseDictEntity::getDictType, dictType)
                .eq(BaseDictEntity::getDictStatus, EnableEnums.ENABLE.getCode())
                .orderByDesc(BaseDictEntity::getSort)
                .last(WebCommonConstants.LIMIT_ONE)
                .one();
        if (baseDictEntity != null) {
            return baseDictEntity.getSort() + 1;
        }
        // 没有数据的话，默认返回1
        return 1;
    }
}
