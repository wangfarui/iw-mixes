package com.itwray.iw.web.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.enums.DictBusinessTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典业务关联表
 *
 * @author wray
 * @since 2024-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_dict_business_relation")
public class BaseDictBusinessRelationEntity extends IdEntity<Integer> {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 业务类型(枚举code)
     *
     * @see DictBusinessTypeEnum#getCode()
     */
    private Integer businessType;

    /**
     * 业务表id
     */
    private Integer businessId;

    /**
     * 字典id
     */
    private Integer dictId;
}
