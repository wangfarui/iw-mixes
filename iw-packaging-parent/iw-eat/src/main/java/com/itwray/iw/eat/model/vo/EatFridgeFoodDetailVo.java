package com.itwray.iw.eat.model.vo;

import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 冰箱食材表 详情VO
 *
 * @author wray
 * @since 2026-01-20
 */
@Data
@Schema(name = "冰箱食材表 详情VO")
public class EatFridgeFoodDetailVo implements DetailVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "食材名称")
    private String name;

    @Schema(title = "食材图标")
    private String emoji;

    @Schema(title = "食材分类")
    private Integer category;

    @Schema(title = "食材分区")
    private Integer section;

    @Schema(title = "数量")
    private String quantity;

    @Schema(title = "入库日期")
    private LocalDate addDate;

    @Schema(title = "过期日期")
    private LocalDate expireDate;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

}
