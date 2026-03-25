package com.itwray.iw.web.core.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户共享查询策略
 *
 * @author wray
 * @since 2026/3/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSharedQueryPolicy {

    /**
     * 当前家庭组ID
     */
    private Integer currentGroupId;

    /**
     * 是否强制仅查看自己
     */
    private boolean forceQueryOnlyMyself;
}
