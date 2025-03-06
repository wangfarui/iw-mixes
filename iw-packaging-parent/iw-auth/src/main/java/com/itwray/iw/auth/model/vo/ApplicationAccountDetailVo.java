package com.itwray.iw.auth.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.DetailVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用账号信息 新增DTO
 *
 * @author wray
 * @since 2025/3/6
 */
@Data
public class ApplicationAccountDetailVo implements DetailVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用地址
     */
    private String address;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    private LocalDateTime updateTime;
}
