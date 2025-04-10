package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itwray.iw.web.json.serialize.CommaSpliceToCollectionSerializer;
import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账行为表 详情VO
 *
 * @author wray
 * @since 2025-04-08
 */
@Data
@Schema(name = "记账行为表 详情VO")
public class BookkeepingActionsDetailVo implements DetailVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "记录类型(1:支出, 2:收入)")
    private Integer recordCategory;

    @Schema(title = "记录来源")
    private String recordSource;

    @Schema(title = "记录分类")
    private Integer recordType;

    @Schema(title = "记录图标")
    private String recordIcon;

    @Schema(title = "记录标签(标签字典id逗号拼接)")
    @JsonSerialize(using = CommaSpliceToCollectionSerializer.class)
    private String recordTags;

    @Schema(title = "排序 0-默认排序")
    private BigDecimal sort;

}
