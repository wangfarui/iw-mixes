package com.itwray.iw.auth.model.vo;

import lombok.Data;

/**
 * 家庭组共享保存策略 VO
 *
 * @author wray
 * @since 2026/3/24
 */
@Data
public class FamilySharedSavePolicyVo {

    /**
     * 当前家庭组ID
     */
    private Integer currentGroupId;

    /**
     * 当前用户新建默认共享开关(0关闭 1开启)
     */
    private Integer defaultShared;

    /**
     * 是否强制共享(0否 1是)
     */
    private Integer forceShared;
}
