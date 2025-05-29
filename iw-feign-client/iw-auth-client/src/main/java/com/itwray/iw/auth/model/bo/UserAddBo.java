package com.itwray.iw.auth.model.bo;

import com.itwray.iw.web.model.dto.UserDto;
import lombok.Data;

/**
 * 用户新增对象
 *
 * @author wray
 * @see com.itwray.iw.auth.dao.AuthUserDao#addNewUser(UserAddBo)
 * @since 2024/12/18
 */
@SuppressWarnings("all")
@Data
public class UserAddBo implements UserDto {

    /**
     * 当前注册的用户id
     */
    private Integer userId;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 邮箱地址
     */
    private String emailAddress;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像（url地址）
     */
    private String avatar;
}
