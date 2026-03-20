package com.itwray.iw.web.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 带用户归属信息的分页记录响应对象基类
 *
 * @author wray
 * @since 2026/3/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserOwnerPageVo<ID extends Serializable> extends AbstractUserOwnerVo implements PageRecordVo {

    /**
     * 主键id
     */
    private ID id;
}
