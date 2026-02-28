package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 网站导航状态枚举
 *
 * @author wray
 * @since 2026/2/28
 */
@Getter
public enum WebsiteNavigationStatusEnum implements BusinessConstantEnum {

    ONLINE(1, "在线"),
    OFFLINE(2, "离线"),
    ;

    private final Integer code;

    private final String name;

    WebsiteNavigationStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
