package com.itwray.iw.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itwray.iw.auth.mapper.BaseFileRecordMapper;
import com.itwray.iw.auth.model.entity.BaseFileRecordEntity;
import org.springframework.stereotype.Component;

/**
 * 文件上传记录表 DAO
 *
 * @author wray
 * @since 2024/5/17
 */
@Component
public class BaseFileRecordDao extends ServiceImpl<BaseFileRecordMapper, BaseFileRecordEntity> {

}
