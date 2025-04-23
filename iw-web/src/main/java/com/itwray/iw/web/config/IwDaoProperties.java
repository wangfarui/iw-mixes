package com.itwray.iw.web.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.core.mybatis.UserDataPermissionHandler;
import com.itwray.iw.web.model.entity.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IW Dao 属性配置
 *
 * @author wray
 * @since 2024/9/7
 */
@ConfigurationProperties(prefix = "iw.dao")
@Validated
@Data
public class IwDaoProperties {

    /**
     * 启用分页插件(默认true)
     */
    private boolean enablePagination = true;

    /**
     * 数据权限
     */
    private DataPermission dataPermission = new DataPermission();

    /**
     * 数据表的数据权限配置
     */
    @Data
    public static class DataPermission {

        /**
         * 启用数据权限(默认false)
         * <p>只有在{@code enabled=true}时，enableTableNames和disableTableNames 才会生效，如果两个列表数据都为空，则全部数据表启用数据权限。</p>
         */
        private boolean enabled = false;

        /**
         * 启用数据权限的表名
         * <p>用于只希望启用个别数据表的数据权限。</p>
         */
        private Set<String> enableTableNames;

        /**
         * 禁用数据权限的表名
         * <p>用于只希望禁用个别数据表的数据权限。</p>
         * <p>更希望web服务针对禁用表做控制。</p>
         * <p>禁用数据表列表的优先级大于启用数据表列表。换言之，只要disableTableNames配置了表a，则表a禁用，如果没有配置，而enableTableNames不为空且未配置，也表示禁用。</p>
         */
        private Set<String> disableTableNames;

        /**
         * 被禁用数据权限的数据表状态缓存
         * <p>涉及到多线程并发: {@link UserDataPermissionHandler#getSqlSegment}</p>
         * true -> 禁用;
         * false -> 启用;
         * null -> 未查询过;
         */
        @Transient
        private Map<String, Boolean> disableTableStatusCache = new ConcurrentHashMap<>();

        /**
         * 默认禁用数据表
         * <p>默认是不带数据权限的，具体权限由业务决定</p>
         */
        private static final Set<String> DEFAULT_DISABLE_TABLE_NAMES = new HashSet<>();

        /**
         * 默认启用数据表
         * <p>默认是带数据权限的，具体权限由业务决定</p>
         */
        private static final Set<String> DEFAULT_ENABLE_TABLE_NAMES = new HashSet<>();

        static {
            List<Class<? extends IdEntity<?>>> idEntityList = Arrays.asList(BaseDictBusinessRelationEntity.class);
            for (Class<? extends IdEntity<?>> clazz : idEntityList) {
                TableName tableName = AnnotationUtils.findAnnotation(clazz, TableName.class);
                if (tableName != null) {
                    DEFAULT_DISABLE_TABLE_NAMES.add(tableName.value());
                }
            }

            List<Class<? extends UserEntity<?>>> userEntityList = Arrays.asList(BaseDictEntity.class, BaseBusinessFileEntity.class);
            for (Class<? extends UserEntity<?>> clazz : userEntityList) {
                TableName tableName = AnnotationUtils.findAnnotation(clazz, TableName.class);
                if (tableName != null) {
                    DEFAULT_ENABLE_TABLE_NAMES.add(tableName.value());
                }
            }
        }

        /**
         * 数据表的数据权限是否被禁用
         *
         * @param tableName 数据表表名
         * @return true -> 被禁用
         */
        public boolean disableTable(String tableName) {
            return disableTableStatusCache.computeIfAbsent(tableName, key -> {
                // 默认被禁用数据权限的表
                if (DEFAULT_DISABLE_TABLE_NAMES.contains(key)) {
                    return true;
                }
                // 默认被启用数据权限的表
                if (DEFAULT_ENABLE_TABLE_NAMES.contains(key)) {
                    return false;
                }
                // 禁用的数据表
                if (CollUtil.isNotEmpty(disableTableNames) && disableTableNames.contains(key)) {
                    return true;
                }
                // 启用的数据表
                return CollUtil.isNotEmpty(enableTableNames) && !enableTableNames.contains(key);
            });
        }
    }
}
