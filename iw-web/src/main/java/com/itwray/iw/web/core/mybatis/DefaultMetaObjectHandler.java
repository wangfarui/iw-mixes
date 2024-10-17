package com.itwray.iw.web.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itwray.iw.web.utils.UserUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * Mybatis-Plus自动填充字段的默认处理器
 *
 * @author wray
 * @since 2024/9/26
 */
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "userId", Integer.class, UserUtils.getUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
