package com.itwray.iw.web.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 字典业务关联表
 *
 * @author wray
 * @since 2024-05-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("base_dict_business_relation")
public class BaseDictBusinessRelationEntity extends IdEntity {

    /**
     * 业务表id
     */
    private Integer id;

    /**
     * 字典id
     */
    private Integer dictId;
}
