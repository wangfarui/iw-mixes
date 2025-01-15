package com.itwray.iw.web.core.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.itwray.iw.web.annotation.IgnorePermission;
import com.itwray.iw.web.config.IwDaoProperties;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Mybatis-Plus配置类
 */
@Configuration
@MapperScan(basePackages = {"com.itwray.iw.web.mapper"})
@ComponentScan(basePackages = "com.itwray.iw.web.dao")
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class MybatisPlusConfiguration {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(IwDaoProperties daoProperties, ConfigurableApplicationContext applicationContext) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (daoProperties.getDataPermission().isEnabled()) {
            Set<String> ignoreUserDataPermissionMethods = this.scanIgnoreUserDataPermissionMethods(applicationContext);
            UserDataPermissionHandler handler = new UserDataPermissionHandler(daoProperties.getDataPermission(), ignoreUserDataPermissionMethods);
            DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor(handler);
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

    /**
     * 扫描忽略用户数据权限的方法
     *
     * @param applicationContext spring应用上下文
     * @return 忽略用户权限的全量方法名
     */
    private Set<String> scanIgnoreUserDataPermissionMethods(ConfigurableApplicationContext applicationContext) {
        Set<String> ignoreUserDataPermissionMethods = new HashSet<>();
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(Mapper.class);
        for (String beanName : beanNames) {
            Class<?> beanClass = applicationContext.getType(beanName);
            if (beanClass == null) {
                continue;
            }
            IgnorePermission ignorePermission = beanClass.getAnnotation(IgnorePermission.class);
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(beanClass);
            if (ignorePermission != null && ignorePermission.userDataPermission()) {
                for (Method method : methods) {
                    ignoreUserDataPermissionMethods.add(beanClass.getName() + "." + method.getName());
                }
            } else {
                for (Method method : methods) {
                    IgnorePermission annotation = method.getAnnotation(IgnorePermission.class);
                    if (annotation != null && annotation.userDataPermission()) {
                        ignoreUserDataPermissionMethods.add(beanClass.getName() + "." + method.getName());
                    }
                }
            }
        }
        return ignoreUserDataPermissionMethods;
    }
}