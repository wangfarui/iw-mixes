package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import lombok.Data;

/**
 * 应用账号信息 新增DTO
 *
 * @author wray
 * @since 2025/3/6
 */
@Data
public class ApplicationAccountAddDto implements AddDto {

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
}
