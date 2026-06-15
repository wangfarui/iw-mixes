package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.bookkeeping.model.dto.BookkeepingAssistantConfirmExpenseDto;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingAssistantConfirmExpenseVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingAssistantParseExpenseVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 记账助手服务
 *
 * @author wray
 * @since 2026/4/14
 */
public interface BookkeepingAssistantService {

    /**
     * 解析语音支出记账草稿
     *
     * @param file 音频文件
     * @param durationMs 音频时长
     * @param format 音频格式
     * @param sampleRate 采样率
     * @param autoSave 解析明确时是否直接保存
     * @return 解析结果
     */
    BookkeepingAssistantParseExpenseVo parseExpenseAudio(MultipartFile file, Integer durationMs, String format, Integer sampleRate, Boolean autoSave);

    /**
     * 确认语音支出记账草稿并保存
     *
     * @param dto 确认请求
     * @return 确认结果
     */
    BookkeepingAssistantConfirmExpenseVo confirmExpense(BookkeepingAssistantConfirmExpenseDto dto);
}
