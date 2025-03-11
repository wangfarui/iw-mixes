package com.itwray.iw.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.dao.BaseApplicationAccountDao;
import com.itwray.iw.auth.mapper.BaseApplicationAccountMapper;
import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.auth.model.dto.ApplicationAccountAddDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountPageDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountRefreshPasswordDto;
import com.itwray.iw.auth.model.dto.ApplicationAccountUpdateDto;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.entity.BaseApplicationAccountEntity;
import com.itwray.iw.auth.model.vo.ApplicationAccountDetailVo;
import com.itwray.iw.auth.model.vo.ApplicationAccountPageVo;
import com.itwray.iw.auth.service.AuthVerificationService;
import com.itwray.iw.auth.service.BaseApplicationAccountService;
import com.itwray.iw.common.utils.AESUtils;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.model.vo.PageVo;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.EnvironmentHolder;
import com.itwray.iw.web.utils.UserUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Set;

/**
 * 应用账号信息表 服务实现类
 *
 * @author wray
 * @since 2025-03-06
 */
@Service
@Slf4j
public class BaseApplicationAccountServiceImpl extends WebServiceImpl<BaseApplicationAccountMapper, BaseApplicationAccountEntity, BaseApplicationAccountDao,
        ApplicationAccountAddDto, ApplicationAccountUpdateDto, ApplicationAccountDetailVo, Integer> implements BaseApplicationAccountService, ApplicationListener<EnvironmentChangeEvent> {

    @Value("${iw.auth.application-account.aes-key}")
    private String aesKey;

    @Nonnull
    private SecretKey secretKey;

    private final AuthUserDao authUserDao;

    private AuthVerificationService authVerificationService;

    @Autowired
    public BaseApplicationAccountServiceImpl(BaseApplicationAccountDao baseDao, AuthUserDao authUserDao) {
        super(baseDao);
        this.authUserDao = authUserDao;
    }

    @Autowired
    public void setAuthVerificationService(AuthVerificationService authVerificationService) {
        this.authVerificationService = authVerificationService;
    }

    @PostConstruct
    public void init() {
        secretKey = AESUtils.generateSecretKey(this.aesKey);
    }

    @Override
    @Transactional
    public Integer add(ApplicationAccountAddDto dto) {
        String encryptHex = this.encrypt(dto.getPassword());
        dto.setPassword(encryptHex);
        return super.add(dto);
    }

    @Override
    @Transactional
    public void update(ApplicationAccountUpdateDto dto) {
        String encryptHex = this.encrypt(dto.getPassword());
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
        try {
            return this.decrypt(accountEntity.getPassword());
        } catch (Exception e) {
            log.error("viewPassword解密异常, encryptPd: " + accountEntity.getPassword(), e);
            return "";
        }
    }

    @Override
    @Transactional
    public void refreshPassword(ApplicationAccountRefreshPasswordDto dto) {
        // 校验验证码的正确性
        Integer userId = UserUtils.getUserId();
        AuthUserEntity authUserEntity = authUserDao.getById(userId);
        if (authUserEntity == null) {
            throw new BusinessException("用户不存在，请刷新重试");
        }
        if (!authVerificationService.compareVerificationCode(
                dto.getVerificationCode(),
                authUserEntity.getPhoneNumber(),
                AuthRedisKeyEnum.APPLICATION_ACCOUNT_REFRESH_KEY)) {
            throw new BusinessException("验证码错误");
        }

        List<BaseApplicationAccountEntity> updateEntityList;
        if (dto.isRefreshAll()) {
            updateEntityList = getBaseDao().lambdaQuery()
                    .eq(BaseApplicationAccountEntity::getUserId, UserUtils.getUserId())
                    .select(BaseApplicationAccountEntity::getId, BaseApplicationAccountEntity::getPassword)
                    .list();
        } else if (CollUtil.isNotEmpty(dto.getIdList())) {
            updateEntityList = getBaseDao().lambdaQuery()
                    .in(BaseApplicationAccountEntity::getId, dto.getIdList())
                    .select(BaseApplicationAccountEntity::getId, BaseApplicationAccountEntity::getPassword)
                    .list();
        } else {
            log.warn("refreshPassword入参异常, dto: {}", dto);
            return;
        }

        if (CollUtil.isEmpty(updateEntityList)) {
            return;
        }

        // 如果存在历史AES,则先解密
        if (StringUtils.isNotBlank(dto.getOldAes())) {
            SecretKey key = AESUtils.generateSecretKey(dto.getOldAes());
            for (BaseApplicationAccountEntity entity : updateEntityList) {
                entity.setPassword(AESUtils.decryptAESGCM(key, entity.getPassword()));
            }
        }

        // 使用动态配置的aes 再进行加密
        for (BaseApplicationAccountEntity entity : updateEntityList) {
            if (this.isEncryptPassword(entity.getPassword())) {
                log.warn("refreshPassword 不建议二次加密密码, id: {}", entity.getId());
                continue;
            }
            entity.setPassword(this.encrypt(entity.getPassword()));
        }

        // 更新所有实体对象
        getBaseDao().updateBatchById(updateEntityList);
    }

    private String encrypt(String originalPassword) {
        return AESUtils.encryptAESGCM(this.secretKey, originalPassword);
    }

    private String decrypt(String encryptPassword) {
        return AESUtils.decryptAESGCM(this.secretKey, encryptPassword);
    }

    private boolean isEncryptPassword(String encryptPassword) {
        return AESUtils.isAesGcmEncrypted(this.secretKey, encryptPassword);
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        Set<String> keys = event.getKeys();
        if (keys.contains("iw.auth.application-account.aes-key")) {
            log.info("Nacos 配置已变更: iw.auth.application-account.aes-key");
            this.aesKey = EnvironmentHolder.getProperty("iw.auth.application-account.aes-key");
            this.init();
        }
    }
}
