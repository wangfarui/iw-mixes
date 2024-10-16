package com.itwray.iw.web.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.itwray.iw.web.config.IwDaoProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus配置类
 */
@Configuration
@MapperScan(basePackages = {"com.itwray.iw.web.mapper"})
@ComponentScan(basePackages = "com.itwray.iw.web.dao")
public class MybatisPlusConfig {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(IwDaoProperties daoProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (daoProperties.getDataPermission().isEnabled()) {
            DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor(new UserDataPermissionHandler(daoProperties.getDataPermission()));
            interceptor.addInnerInterceptor(dataPermissionInterceptor);
        }
        // 分页插件需要放到最后面
        if (daoProperties.isEnablePagination()) {
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//如果配置多个插件,切记分页最后添加
        }
        return interceptor;
    }

    /**
     * 默认的自动填充字段处理器
     */
    @Bean
    public DefaultMetaObjectHandler defaultMetaObjectHandler() {
        return new DefaultMetaObjectHandler();
    }
}