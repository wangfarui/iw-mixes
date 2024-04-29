package com.itwray.iw.eat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.eat.mapper.EatUserMapper;
import com.itwray.iw.eat.model.entity.EatUserEntity;
import org.springframework.stereotype.Component;

/**
 * EatUserDao
 *
 * @author wray
 * @since 2024/4/29
 */
@Component
public class EatUserDao extends ServiceImpl<EatUserMapper, EatUserEntity> {
}
