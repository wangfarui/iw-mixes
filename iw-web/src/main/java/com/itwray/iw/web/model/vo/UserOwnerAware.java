package com.itwray.iw.web.model.vo;

/**
 * 带用户归属信息的响应对象
 *
 * @author wray
 * @since 2026/3/19
 */
public interface UserOwnerAware {

    Integer getUserId();

    void setUserId(Integer userId);

    String getUserName();

    void setUserName(String userName);

    Boolean getCanEdit();

    void setCanEdit(Boolean canEdit);
}
