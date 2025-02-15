package com.itwray.iw.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.auth.mapper.BaseFileRecordsMapper;
import com.itwray.iw.auth.model.entity.BaseFileRecordsEntity;
import org.springframework.stereotype.Component;

/**
 * 文件上传记录表 DAO
 *
 * @author wray
 * @since 2024/5/17
 */
@Component
public class BaseFileRecordsDao extends ServiceImpl<BaseFileRecordsMapper, BaseFileRecordsEntity> {

}
