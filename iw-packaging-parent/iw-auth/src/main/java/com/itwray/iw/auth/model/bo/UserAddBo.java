package com.itwray.iw.auth.model.bo;

import com.itwray.iw.auth.dao.AuthUserDao;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户新增对象
 *
 * @author wray
 * @see AuthUserDao#addNewUser(UserAddBo)
 * @since 2024/12/18
 */
@Data
@NoArgsConstructor
public class UserAddBo {

    /**
     * 电话号码
     */
    private String phoneNumber;

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

    /**
     * 推荐使用仅电话号码的UserAddBo实例化对象
     *
     * @param phoneNumber 电话号码
     */
    public UserAddBo(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
