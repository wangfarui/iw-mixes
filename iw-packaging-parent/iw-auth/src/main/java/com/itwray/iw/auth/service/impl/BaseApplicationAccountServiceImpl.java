package com.itwray.iw.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.auth.dao.BaseApplicationAccountDao;
import com.itwray.iw.auth.mapper.BaseApplicationAccountMapper;
import com.itwray.iw.auth.model.dto.ApplicationAccountAddDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountPageDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountUpdateDto;
import com.itwray.iw.auth.model.entity.BaseApplicationAccountEntity;
import com.itwray.iw.auth.model.vo.ApplicationAccountDetailVo;
import com.itwray.iw.auth.model.vo.ApplicationAccountPageVo;
import com.itwray.iw.auth.service.BaseApplicationAccountService;
import com.itwray.iw.web.model.vo.PageVo;
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

    @Override
    public PageVo<ApplicationAccountPageVo> page(ApplicationAccountPageDto dto) {
        LambdaQueryWrapper<BaseApplicationAccountEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(dto.getName() != null, BaseApplicationAccountEntity::getName, dto.getName())
                .like(dto.getAddress() != null, BaseApplicationAccountEntity::getAddress, dto.getAddress());
        queryWrapper.orderByDesc(BaseApplicationAccountEntity::getId);
        return getBaseDao().page(dto, queryWrapper, ApplicationAccountPageVo.class);
    }
}
