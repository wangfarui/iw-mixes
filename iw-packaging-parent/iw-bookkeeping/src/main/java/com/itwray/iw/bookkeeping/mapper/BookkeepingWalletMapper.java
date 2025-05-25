package com.itwray.iw.bookkeeping.mapper;

import com.itwray.iw.bookkeeping.model.entity.BookkeepingWalletEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.web.annotation.IgnorePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 用户钱包表 Mapper 接口
 *
 * @author wray
 * @since 2025-05-22
 */
@Mapper
public interface BookkeepingWalletMapper extends BaseMapper<BookkeepingWalletEntity> {

    @IgnorePermission
    int updateWalletBalance(@Param("userId") Integer userId, @Param("amount") BigDecimal amount);
}
