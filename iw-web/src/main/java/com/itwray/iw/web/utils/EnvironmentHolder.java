package com.itwray.iw.web.utils;

import com.itwray.iw.web.exception.IwWebException;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Spring ConfigurableEnvironment Holder
 *
 * @author wray
 * @since 2024/12/16
 */
@Component
public class EnvironmentHolder implements EnvironmentAware {

    private static ConfigurableEnvironment environment;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
            EnvironmentHolder.environment = configurableEnvironment;
        } else {
            throw new IwWebException("Target Not Found ConfigurableEnvironment");
        }
    }

    public static boolean containsProperty(String key) {
        return environment.containsProperty(key);
    }

    @Nullable
    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    @Nullable
    public static <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    public static String getRequiredProperty(String key) throws IllegalStateException {
        return environment.getRequiredProperty(key);
    }

    public static <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return environment.getRequiredProperty(key, targetType);
    }

    public static String resolvePlaceholders(String text) {
        return environment.resolvePlaceholders(text);
    }

    public static String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return environment.resolveRequiredPlaceholders(text);
    }
}
