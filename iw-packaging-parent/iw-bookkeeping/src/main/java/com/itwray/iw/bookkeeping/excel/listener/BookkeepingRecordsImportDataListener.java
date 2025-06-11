package com.itwray.iw.bookkeeping.excel.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import com.itwray.iw.bookkeeping.model.bo.BookkeepingRecordsImportBo;
import com.itwray.iw.bookkeeping.service.BookkeepingRecordsService;
import com.itwray.iw.web.utils.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 记账记录导入数据监听器
 *
 * @author farui.wang
 * @since 2025/6/9
 */
@Slf4j
public class BookkeepingRecordsImportDataListener implements ReadListener<BookkeepingRecordsImportBo> {

    /**
     * 当前操作人id
     */
    private final Integer userId;

    /**
     * 字典名称类型枚举Map
     */
    private final Map<String, Integer> dictNameMap;

    public BookkeepingRecordsImportDataListener(Integer userId, Map<String, Integer> dictNameMap) {
        this.userId = userId;
        this.dictNameMap = dictNameMap;
    }

    @Override
    public void invoke(BookkeepingRecordsImportBo bookkeepingRecordsImportBo, AnalysisContext analysisContext) {
        bookkeepingRecordsImportBo.setUserId(userId);
        getBookkeepingRecordsService().processImportData(bookkeepingRecordsImportBo, dictNameMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据导入完成");
    }

    private static final class BookkeepingRecordsServiceHolder {
        private static final BookkeepingRecordsService bookkeepingRecordsService = ApplicationContextHolder.getBean(BookkeepingRecordsService.class);
    }

    public static BookkeepingRecordsService getBookkeepingRecordsService() {
        return BookkeepingRecordsServiceHolder.bookkeepingRecordsService;
    }
}
