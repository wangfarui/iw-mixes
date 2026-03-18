package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.web.model.dto.SharedQueryRequest;
import lombok.Data;

import java.time.LocalDate;

/**
 * 记账记录 列表DTO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
public class BookkeepingRecordListDto implements SharedQueryRequest {

    /**
     * 记账日期
     */
    private LocalDate recordDate;

    /**
     * 是否仅查询本人数据
     * <p>默认为null, 表示查询共享口径</p>
     * <p>如果为 {@link com.itwray.iw.common.constants.BoolEnum#TRUE} , 表示仅查询本人数据</p>
     */
    private Integer queryOnlyMyself;
}
