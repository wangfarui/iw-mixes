package com.itwray.iw.external.service;

import com.itwray.iw.external.model.dto.AsrSentenceRecognizeDto;
import com.itwray.iw.external.model.vo.AsrSentenceRecognizeVo;

/**
 * 语音识别服务
 *
 * @author wray
 * @since 2026/4/14
 */
public interface AsrService {

    /**
     * 一句话识别
     *
     * @param dto 请求参数
     * @return 识别结果
     */
    AsrSentenceRecognizeVo sentenceRecognize(AsrSentenceRecognizeDto dto);
}
