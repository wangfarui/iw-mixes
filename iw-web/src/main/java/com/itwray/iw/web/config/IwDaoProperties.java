package com.itwray.iw.web.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.validation.annotation.Validated;

import java.util.*;

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
         * 启用数据权限(默认true)
         * <p>只有在{@code enabled=true}时，enableTableNames和disableTableNames 才会生效，如果两个列表数据都为空，则全部数据表启用数据权限。</p>
         */
        private boolean enabled = true;

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
         * true -> 禁用
         * false -> 启用
         * null -> 未查询过
         */
        @Transient
        private Map<String, Boolean> disableTableStatusCache = new HashMap<>();

        /**
         * 基础数据表
         * <p>默认是不带数据权限的，具体权限由业务决定</p>
         */
        private static final Set<String> BASE_TABLE_NAMES = new HashSet<>();

        static {
            TableName tableName = AnnotationUtils.findAnnotation(BaseDictEntity.class, TableName.class);
            if (tableName != null) {
                BASE_TABLE_NAMES.add(tableName.value());
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
                // 基础数据表默认被禁用数据权限
                if (BASE_TABLE_NAMES.contains(key)) {
                    return true;
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
