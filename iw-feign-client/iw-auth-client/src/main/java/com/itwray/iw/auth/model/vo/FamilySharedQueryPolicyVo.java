package com.itwray.iw.auth.model.vo;

import lombok.Data;

/**
 * 家庭组共享查询策略 VO
 *
 * @author wray
 * @since 2026/3/23
 */
@Data
public class FamilySharedQueryPolicyVo {

    /**
     * 当前家庭组ID
     */
    private Integer currentGroupId;

    /**
     * 是否强制仅查看自己(0否 1是)
     */
    private Integer forceQueryOnlyMyself;
}
