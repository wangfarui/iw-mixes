package com.itwray.iw.bookkeeping.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记账支出 更新DTO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExpensesUpdateDto extends ExpensesAddDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Integer id;
}
