package com.itwray.iw.web.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.enums.BusinessFileTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务文件关联表
 *
 * @author wray
 * @since 2025-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_business_file")
public class BaseBusinessFileEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业务类型
     */
    private BusinessFileTypeEnum businessType;

    /**
     * 业务id
     */
    private Integer businessId;

    /**
     * 文件名称(带后缀)
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String fileUri;
}
