package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.bookkeeping.model.enums.WalletRecordsChangeTypeEnum;
import com.itwray.iw.web.model.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 钱包记录分页请求对象
 *
 * @author farui.wang
 * @since 2025/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "钱包记录分页请求对象")
public class BookkeepingWalletRecordsPageDto extends PageDto {

    @Schema(title = "变动类型")
    @NotNull(message = "变动类型不能为空")
    private WalletRecordsChangeTypeEnum changeType;
}
