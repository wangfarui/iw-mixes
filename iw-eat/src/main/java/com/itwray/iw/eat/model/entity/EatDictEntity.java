package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.itwray.iw.web.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 *
 * @author wray
 * @since 2024-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("eat_dict")
public class EatDictEntity extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父字典id
     */
    private Integer parentId;

    /**
     * 字典类型
     */
    private Integer dictType;

    /**
     * 字典code
     */
    private Integer dictCode;

    /**
     * 字典名称
     */
    private String dictName;

}
