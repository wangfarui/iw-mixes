package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.ApplicationAccountAddDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountUpdateDto;
import com.itwray.iw.auth.model.vo.ApplicationAccountDetailVo;
import com.itwray.iw.web.service.WebService;

/**
 * 应用账号信息表 服务接口
 *
 * @author wray
 * @since 2025-03-06
 */
public interface BaseApplicationAccountService extends WebService<ApplicationAccountAddDto, ApplicationAccountUpdateDto, ApplicationAccountDetailVo, Integer> {

}
