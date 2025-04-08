package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itwray.iw.web.json.deserialize.CollectionToCommaSpliceDeserializer;
import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 记账行为表 新增DTO
 *
 * @author wray
 * @since 2025-04-08
 */
@Data
@Schema(name = "记账行为表 新增DTO")
public class BookkeepingActionsAddDto implements AddDto {

    @Schema(title = "记录类型(1:支出, 2:收入)")
    @NotNull(message = "记录类型不能为空")
    private Integer recordCategory;

    @Schema(title = "记录来源")
    @NotBlank(message = "记录来源不能为空")
    private String recordSource;

    @Schema(title = "记录分类")
    @NotNull(message = "记录分类不能为空")
    private Integer recordType;

    @Schema(title = "记录图标")
    private String recordIcon;

    @Schema(title = "记录标签id集合")
    @JsonDeserialize(using = CollectionToCommaSpliceDeserializer.class)
    private String recordTags;

    @Schema(title = "排序 0-默认排序")
    private Integer sort;

}
