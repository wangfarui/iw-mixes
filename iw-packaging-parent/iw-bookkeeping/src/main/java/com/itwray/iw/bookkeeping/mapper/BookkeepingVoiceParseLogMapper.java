package com.itwray.iw.bookkeeping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingVoiceParseLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 语音记账解析日志 Mapper
 *
 * @author wray
 * @since 2026/4/14
 */
@Mapper
public interface BookkeepingVoiceParseLogMapper extends BaseMapper<BookkeepingVoiceParseLogEntity> {
}
