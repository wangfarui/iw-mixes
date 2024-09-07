package com.itwray.iw.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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
     * 启用分页插件
     */
    private boolean enablePagination = true;

    /**
     * 数据权限
     */
    private DataPermission dataPermission = new DataPermission();

    @Data
    public static class DataPermission {

        /**
         * 启用数据权限
         */
        private boolean enabled = true;

        /**
         * 禁用数据权限的表名
         */
        private List<String> disableTableNames;

        /**
         * 数据表的数据权限是否被禁用
         *
         * @param tableName 数据表表名
         * @return true -> 被禁用
         */
        public boolean disableTable(String tableName) {
            return disableTableNames != null && disableTableNames.contains(tableName);
        }
    }
}
