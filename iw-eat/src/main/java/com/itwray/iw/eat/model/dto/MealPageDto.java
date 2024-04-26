package com.itwray.iw.eat.model.dto;

import com.itwray.iw.web.model.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 用餐分页请求对象
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MealPageDto extends PageDto {

    /**
     * 用餐日期
     */
    private LocalDate mealDate;
}
