package com.itwray.iw.auth.core.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

class CaptchaAuthenticationToken extends AbstractAuthenticationToken {

    private final String captcha;

    public CaptchaAuthenticationToken(String captcha) {
        super(null);
        this.captcha = captcha;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    public String getCaptcha() {
        return captcha;
    }
}