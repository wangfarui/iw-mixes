package com.itwray.iw.eat.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.eat.model.enums.FridgeFoodSortTypeEnum;
import com.itwray.iw.web.model.dto.PageDto;
import com.itwray.iw.web.model.enums.SortWayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 冰箱食材表 分页DTO
 *
 * @author wray
 * @since 2026/1/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EatFridgeFoodPageDto extends PageDto {

    @Schema(title = "食材名称")
    private String name;

    @Schema(title = "食材分类")
    private Integer category;

    @Schema(title = "食材分区")
    private Integer section;

    @Schema(title = "过期开始日期")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate expireStartDate;

    @Schema(title = "过期结束日期")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate expireEndDate;

    @Schema(title = "排序类型")
    private FridgeFoodSortTypeEnum sortType;

    @Schema(title = "排序方式 1=升序, 其他值=降序")
    private SortWayEnum sortWay;
}
