package com.itwray.iw.web.core.feign;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeImpl implements ParameterizedType {

    private final Type raw;
    private final Type[] args;
    private final Type owner;

    private ParameterizedTypeImpl(Type raw, Type[] args, Type owner) {
        this.raw = raw;
        this.args = args;
        this.owner = owner;
    }

    public static ParameterizedTypeImpl make(Type raw, Type[] args, Type owner) {
        return new ParameterizedTypeImpl(raw, args, owner);
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return owner;
    }
}
