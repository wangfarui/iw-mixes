package com.itwray.iw.auth.service.impl;

import com.itwray.iw.auth.dao.BaseApplicationAccountDao;
import com.itwray.iw.auth.mapper.BaseApplicationAccountMapper;
import com.itwray.iw.auth.model.dto.ApplicationAccountAddDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountUpdateDto;
import com.itwray.iw.auth.model.entity.BaseApplicationAccountEntity;
import com.itwray.iw.auth.model.vo.ApplicationAccountDetailVo;
import com.itwray.iw.auth.service.BaseApplicationAccountService;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 应用账号信息表 服务实现类
 *
 * @author wray
 * @since 2025-03-06
 */
@Service
public class BaseApplicationAccountServiceImpl extends WebServiceImpl<BaseApplicationAccountMapper, BaseApplicationAccountEntity, BaseApplicationAccountDao,
        ApplicationAccountAddDto, ApplicationAccountUpdateDto, ApplicationAccountDetailVo, Integer> implements BaseApplicationAccountService {

    @Autowired
    public BaseApplicationAccountServiceImpl(BaseApplicationAccountDao baseDao) {
        super(baseDao);
    }
    

}
