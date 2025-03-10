package com.itwray.iw.auth.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
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
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 应用账号信息表 服务实现类
 *
 * @author wray
 * @since 2025-03-06
 */
@Service
public class BaseApplicationAccountServiceImpl extends WebServiceImpl<BaseApplicationAccountMapper, BaseApplicationAccountEntity, BaseApplicationAccountDao,
        ApplicationAccountAddDto, ApplicationAccountUpdateDto, ApplicationAccountDetailVo, Integer> implements BaseApplicationAccountService {

    @Value("${iw.auth.application-account.aes-key}")
    private String aesKey;

    @Nonnull
    private SymmetricCrypto aes;

    @Autowired
    public BaseApplicationAccountServiceImpl(BaseApplicationAccountDao baseDao) {
        super(baseDao);
    }

    @PostConstruct
    public void init() {
        String fullAesKey = StringUtils.rightPad(aesKey, 32, "*");
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), fullAesKey.getBytes(StandardCharsets.UTF_8)).getEncoded();
        aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
    }

    @Override
    public Integer add(ApplicationAccountAddDto dto) {
        String encryptHex = aes.encryptHex(dto.getPassword());
        dto.setPassword(encryptHex);
        return super.add(dto);
    }

    @Override
    public void update(ApplicationAccountUpdateDto dto) {
        String encryptHex = aes.encryptHex(dto.getPassword());
        dto.setPassword(encryptHex);
        super.update(dto);
    }

    @Override
    public PageVo<ApplicationAccountPageVo> page(ApplicationAccountPageDto dto) {
        LambdaQueryWrapper<BaseApplicationAccountEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(dto.getName() != null, BaseApplicationAccountEntity::getName, dto.getName())
                .like(dto.getAddress() != null, BaseApplicationAccountEntity::getAddress, dto.getAddress());
        queryWrapper.orderByDesc(BaseApplicationAccountEntity::getId);
        return getBaseDao().page(dto, queryWrapper, ApplicationAccountPageVo.class);
    }

    @Override
    public String viewPassword(Integer id) {
        BaseApplicationAccountEntity accountEntity = getBaseDao().queryById(id);
        return aes.decryptStr(accountEntity.getPassword());
    }
}
