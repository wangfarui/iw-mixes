package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.ApplicationAccountAddDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountPageDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountRefreshPasswordDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountUpdateDto;
import com.itwray.iw.auth.model.vo.ApplicationAccountDetailVo;
import com.itwray.iw.auth.model.vo.ApplicationAccountPageVo;
import com.itwray.iw.auth.service.BaseApplicationAccountService;
import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.web.controller.WebController;
import com.itwray.iw.web.model.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 应用账号信息表 接口控制层
 *
 * @author wray
 * @since 2025-03-06
 */
@RestController
@RequestMapping("/applicationAccount")
@Validated
@Tag(name = "应用账号信息接口")
public class BaseApplicationAccountController extends WebController<BaseApplicationAccountService,
        ApplicationAccountAddDto, ApplicationAccountUpdateDto, ApplicationAccountDetailVo, Integer> {

    @Autowired
    public BaseApplicationAccountController(BaseApplicationAccountService webService) {
        super(webService);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询应用账号信息")
    public PageVo<ApplicationAccountPageVo> page(@RequestBody @Valid ApplicationAccountPageDto dto) {
        return getWebService().page(dto);
    }

    @GetMapping("/viewPassword")
    @Operation(summary = "查询原文Password")
    public GeneralResponse<String> viewPassword(@RequestParam("id") Integer id) {
        String password = getWebService().viewPassword(id);
        return GeneralResponse.success(password);
    }

    @PostMapping("/refreshPassword")
    @Operation(summary = "刷新密码的加密存储数据")
    public void refreshPassword(@RequestBody @Valid ApplicationAccountRefreshPasswordDto dto) {
        getWebService().refreshPassword(dto);
    }

}
