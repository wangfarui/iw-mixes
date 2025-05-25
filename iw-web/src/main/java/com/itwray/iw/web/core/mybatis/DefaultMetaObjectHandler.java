package com.itwray.iw.web.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itwray.iw.web.model.entity.BaseEntity;
import com.itwray.iw.web.model.entity.UserEntity;
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
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof BaseEntity) {
            LocalDateTime now = LocalDateTime.now();
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        }
        if (originalObject instanceof UserEntity<?> userEntity) {
            if (userEntity.getUserId() == null) {
                this.strictInsertFill(metaObject, "userId", Integer.class, UserUtils.getUserId());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof BaseEntity) {
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }
}
