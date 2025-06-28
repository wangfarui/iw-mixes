package com.itwray.iw.bookkeeping.model.bo;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.format.DateTimeFormat;
import com.itwray.iw.bookkeeping.excel.converter.BookkeepingRecordCategoryConverter;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 记账记录导入对象
 *
 * @author farui.wang
 * @since 2025/6/9
 */
@Data
public class BookkeepingRecordsImportBo {

    @ExcelProperty("记录时间")
    @DateTimeFormat("yyyy/m/d h:mm")
    private LocalDateTime recordTime;

    @ExcelProperty("分类")
    private String recordTypeDesc;

    @ExcelProperty(value = "收支类型", converter = BookkeepingRecordCategoryConverter.class)
    private RecordCategoryEnum recordCategory;

    @ExcelProperty("金额")
    private BigDecimal amount;

    @ExcelProperty("备注")
    private String remark;

    @ExcelIgnore
    private Integer userId;

}
