package com.itwray.iw.auth.test;

import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthRegisterService;
import com.itwray.iw.auth.service.AuthUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 认证服务测试类
 *
 * @author wray
 * @since 2024/3/12
 */
@SpringBootTest
public class AuthUserEntityServiceTest {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthRegisterService authRegisterService;

    @Test
    public void testRegisterByForm() {
        String clientIp = "0.0.0.0";
        String phoneNumber = "13312345678";
        RegisterFormDto dto = new RegisterFormDto();
        dto.setUsername("wray");
        dto.setPassword("123456");
        dto.setPhoneNumber(phoneNumber);
        dto.setVerificationCode("111111");
        authRegisterService.registerByForm(dto, clientIp);
        System.out.println("注册成功");
    }

    @Test
    public void testGetVerificationCode() {
        String clientIp = "0.0.0.0";
        String phoneNumber = "13312345678";
        String result = authRegisterService.getVerificationCode(phoneNumber, clientIp);
        System.out.println(result);
    }

    @Test
    public void testLoginByPassword() {
        LoginPasswordDto dto = new LoginPasswordDto();
        dto.setAccount("wray");
        dto.setPassword("123456");
        UserInfoVo vo = authUserService.loginByPassword(dto);
        System.out.println(vo);
    }
}
