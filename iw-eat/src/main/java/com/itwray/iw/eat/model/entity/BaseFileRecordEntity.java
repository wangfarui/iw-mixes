package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 文件上传记录表
 *
 * @author wray
 * @since 2024/5/17
 */
@Data
@TableName("base_file_record")
public class BaseFileRecordEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件hash二进制值
     */
    private byte[] fileHash;

    /**
     * 文件路径
     */
    private String fileUri;

    /**
     * 文件前缀
     */
    private String filePrefix;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 创建时间
     */
    private Date createTime;
}
