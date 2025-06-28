package com.itwray.iw.bookkeeping.excel.converter;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;

/**
 * 记账-记录分类转换器
 *
 * @author farui.wang`
 * @since 2025/6/10
 */
public class BookkeepingRecordCategoryConverter implements Converter<RecordCategoryEnum> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return RecordCategoryEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public RecordCategoryEnum convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getStringValue();
        if (stringValue == null) {
            return null;
        }
        if ("支出".equals(stringValue)) {
            return RecordCategoryEnum.CONSUME;
        } else if ("收入".equals(stringValue)) {
            return RecordCategoryEnum.INCOME;
        } else {
            return null;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(RecordCategoryEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.getName());
    }
}
