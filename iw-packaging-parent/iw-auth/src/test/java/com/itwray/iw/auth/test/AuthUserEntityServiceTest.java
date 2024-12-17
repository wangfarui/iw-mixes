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
        RegisterFormDto dto = new RegisterFormDto();
        dto.setUsername("wray");
        dto.setPassword("123456");
        authRegisterService.registerByForm(dto, "0.0.0.0");
        System.out.println("注册成功");
    }

    @Test
    public void testLoginByPassword() {
        LoginPasswordDto dto = new LoginPasswordDto();
        dto.setUsername("wray");
        dto.setPassword("123456");
        UserInfoVo vo = authUserService.loginByPassword(dto);
        System.out.println(vo);
    }
}
