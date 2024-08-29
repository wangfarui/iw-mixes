package com.itwray.iw.bookkeeping.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记账记录 更新DTO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookkeepingRecordUpdateDto extends BookkeepingRecordAddDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Integer id;
}
