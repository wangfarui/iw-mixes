package com.itwray.iw.web.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.enums.DictTypeEnum;
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
@TableName("base_dict")
public class BaseDictEntity extends UserEntity<Integer> {

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
     *
     * @see DictTypeEnum#getCode()
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

    /**
     * 字典状态(0禁用 1启用)
     */
    private Integer dictStatus;

    /**
     * 排序
     */
    private Integer sort;

}
