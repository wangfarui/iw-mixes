package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.json.JSONUtil;
import com.itwray.iw.auth.client.AuthFamilyGroupClient;
import com.itwray.iw.auth.client.BaseDictClient;
import com.itwray.iw.auth.model.vo.DictListVo;
import com.itwray.iw.auth.model.vo.FamilySharedSavePolicyVo;
import com.itwray.iw.bookkeeping.dao.BookkeepingActionsDao;
import com.itwray.iw.bookkeeping.dao.BookkeepingVoiceParseLogDao;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingAssistantConfirmExpenseDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingActionsEntity;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingVoiceParseLogEntity;
import com.itwray.iw.bookkeeping.model.enums.BookkeepingAssistantConfirmStatusEnum;
import com.itwray.iw.bookkeeping.model.enums.BookkeepingAssistantParseStatusEnum;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingAssistantConfirmExpenseVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingAssistantExpenseDraftVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingAssistantParseExpenseVo;
import com.itwray.iw.bookkeeping.service.BookkeepingAssistantRemoteService;
import com.itwray.iw.bookkeeping.service.BookkeepingAssistantService;
import com.itwray.iw.bookkeeping.service.BookkeepingRecordsService;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.external.model.dto.AiChatMessageDto;
import com.itwray.iw.external.model.dto.AiStructuredChatDto;
import com.itwray.iw.external.model.dto.AsrSentenceRecognizeDto;
import com.itwray.iw.external.model.vo.AiStructuredChatVo;
import com.itwray.iw.external.model.vo.AsrSentenceRecognizeVo;
import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.model.enums.DictTypeEnum;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 记账助手服务实现
 *
 * @author wray
 * @since 2026/4/14
 */
@Service
@Slf4j
public class BookkeepingAssistantServiceImpl implements BookkeepingAssistantService {

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(\\d+(?:\\.\\d{1,2})?)");

    private static final BigDecimal AUTO_SAVE_CONFIDENCE_THRESHOLD = new BigDecimal("0.85");

    private final BookkeepingAssistantRemoteService bookkeepingAssistantRemoteService;

    private final BookkeepingVoiceParseLogDao bookkeepingVoiceParseLogDao;

    private final BookkeepingActionsDao bookkeepingActionsDao;

    private final BaseDictClient baseDictClient;

    private final AuthFamilyGroupClient authFamilyGroupClient;

    private final BookkeepingRecordsService bookkeepingRecordsService;

    public BookkeepingAssistantServiceImpl(BookkeepingAssistantRemoteService bookkeepingAssistantRemoteService,
                                           BookkeepingVoiceParseLogDao bookkeepingVoiceParseLogDao,
                                           BookkeepingActionsDao bookkeepingActionsDao,
                                           BaseDictClient baseDictClient,
                                           AuthFamilyGroupClient authFamilyGroupClient,
                                           BookkeepingRecordsService bookkeepingRecordsService) {
        this.bookkeepingAssistantRemoteService = bookkeepingAssistantRemoteService;
        this.bookkeepingVoiceParseLogDao = bookkeepingVoiceParseLogDao;
        this.bookkeepingActionsDao = bookkeepingActionsDao;
        this.baseDictClient = baseDictClient;
        this.authFamilyGroupClient = authFamilyGroupClient;
        this.bookkeepingRecordsService = bookkeepingRecordsService;
    }

    @Override
    @Transactional
    public BookkeepingAssistantParseExpenseVo parseExpenseAudio(MultipartFile file,
                                                                Integer durationMs,
                                                                String format,
                                                                Integer sampleRate,
                                                                Boolean autoSave) {
        if (file == null || file.isEmpty()) {
            throw new IwWebException("音频文件不能为空");
        }

        String recognizedText = null;
        AiStructuredChatVo aiResponse = null;
        BookkeepingAssistantExpenseDraftVo draftVo = new BookkeepingAssistantExpenseDraftVo();
        List<String> missingFields = new ArrayList<>();
        List<String> ambiguities = new ArrayList<>();
        Integer matchedActionId = null;
        BigDecimal confidence = new BigDecimal("0.40");
        BookkeepingAssistantParseStatusEnum status = BookkeepingAssistantParseStatusEnum.NEED_CONFIRM;
        String message = "请确认后保存";

        try {
            AsrSentenceRecognizeDto asrDto = new AsrSentenceRecognizeDto();
            asrDto.setAudioBase64(Base64.getEncoder().encodeToString(file.getBytes()));
            asrDto.setFormat(StringUtils.defaultIfBlank(format, this.resolveFormat(file.getOriginalFilename())));
            asrDto.setSampleRate(sampleRate);
            AsrSentenceRecognizeVo asrVo = bookkeepingAssistantRemoteService.sentenceRecognition(asrDto);
            recognizedText = StringUtils.trimToEmpty(asrVo.getResult());
            if (StringUtils.isBlank(recognizedText)) {
                status = BookkeepingAssistantParseStatusEnum.NEED_MORE_INFO;
                message = "未识别到有效语音内容";
                missingFields.add("recognizedText");
            } else {
                draftVo.setRecordCategory(RecordCategoryEnum.CONSUME.getCode());
                draftVo.setRecordDate(this.resolveRecordDate(recognizedText));
                draftVo.setIsStatistics(BoolEnum.TRUE.getCode());
                draftVo.setShared(this.resolveDefaultShared());

                BigDecimal amount = this.extractAmount(recognizedText);
                if (amount != null) {
                    draftVo.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
                    confidence = new BigDecimal("0.65");
                }

                List<BookkeepingActionsEntity> actionList = this.queryExpenseActionList();
                List<DictListVo> recordTypeList = baseDictClient.getDictListByType(DictTypeEnum.BOOKKEEPING_RECORD_TYPE.getCode());
                aiResponse = bookkeepingAssistantRemoteService.structuredChat(this.buildStructuredChatDto(recognizedText, amount, actionList, recordTypeList));

                matchedActionId = this.applyAiResult(aiResponse == null ? null : aiResponse.getContent(), actionList, recordTypeList, draftVo, ambiguities);

                if (draftVo.getAmount() != null && draftVo.getRecordType() != null && StringUtils.isNotBlank(draftVo.getRecordSource())) {
                    status = ambiguities.isEmpty() ? BookkeepingAssistantParseStatusEnum.READY : BookkeepingAssistantParseStatusEnum.NEED_CONFIRM;
                    message = ambiguities.isEmpty() ? "已生成记账草稿" : "已生成记账草稿，请确认分类";
                    confidence = ambiguities.isEmpty() ? new BigDecimal("0.88") : new BigDecimal("0.72");
                } else {
                    if (draftVo.getAmount() == null) {
                        missingFields.add("amount");
                        status = BookkeepingAssistantParseStatusEnum.NEED_MORE_INFO;
                        message = "暂未识别到明确金额";
                    }
                    if (draftVo.getRecordType() == null) {
                        missingFields.add("recordType");
                    }
                    if (StringUtils.isBlank(draftVo.getRecordSource())) {
                        missingFields.add("recordSource");
                    }
                    if (status == BookkeepingAssistantParseStatusEnum.READY) {
                        status = BookkeepingAssistantParseStatusEnum.NEED_CONFIRM;
                    }
                }
            }
        } catch (IOException e) {
            throw new IwWebException("读取音频文件失败");
        } catch (IwWebException e) {
            log.warn("BookkeepingAssistantService#parseExpenseAudio 解析语音支出记账失败, message: {}", e.getMessage());
            status = BookkeepingAssistantParseStatusEnum.FAILED;
            message = this.resolveParseFailureMessage(e.getMessage());
            ambiguities.add("serviceException");
        } catch (Exception e) {
            log.error("BookkeepingAssistantService#parseExpenseAudio 解析语音支出记账异常", e);
            status = BookkeepingAssistantParseStatusEnum.FAILED;
            message = "语音解析失败，请重试";
            ambiguities.add("serviceException");
        }

        BookkeepingVoiceParseLogEntity logEntity = new BookkeepingVoiceParseLogEntity();
        logEntity.setParseStatus(status.name());
        logEntity.setConfirmStatus(BookkeepingAssistantConfirmStatusEnum.UNCONFIRMED.name());
        logEntity.setRecognizedText(recognizedText);
        logEntity.setAudioFormat(StringUtils.defaultIfBlank(format, this.resolveFormat(file.getOriginalFilename())));
        logEntity.setAudioDurationMs(durationMs);
        logEntity.setConfidence(confidence);
        logEntity.setMatchedActionId(matchedActionId);
        logEntity.setDraftJson(JSONUtil.toJsonStr(draftVo));
        logEntity.setWarningJson(this.buildWarningJson(missingFields, ambiguities));
        logEntity.setAiRawResponse(aiResponse == null ? null : aiResponse.getContent());
        logEntity.setProvider("aliyun-asr+deepseek-chat");
        bookkeepingVoiceParseLogDao.save(logEntity);

        boolean autoSaveEligible = this.isAutoSaveEligible(status, confidence, draftVo, missingFields, ambiguities);
        BookkeepingAssistantParseExpenseVo vo = new BookkeepingAssistantParseExpenseVo();
        vo.setLogId(logEntity.getId());
        vo.setStatus(status.name());
        vo.setRecognizedText(recognizedText);
        vo.setConfidence(confidence);
        vo.setMatchedActionId(matchedActionId);
        vo.setMessage(message);
        vo.setAutoSaveEligible(autoSaveEligible);
        vo.setAutoSaved(Boolean.FALSE);
        vo.setConfirmReused(Boolean.FALSE);
        vo.setMissingFields(missingFields);
        vo.setAmbiguities(ambiguities);
        vo.setDraft(draftVo);

        if (Boolean.TRUE.equals(autoSave) && autoSaveEligible) {
            try {
                BookkeepingAssistantConfirmExpenseVo confirmVo = this.confirmExpense(this.buildConfirmExpenseDto(logEntity.getId(), draftVo));
                vo.setAutoSaved(Boolean.TRUE);
                vo.setRecordId(confirmVo.getRecordId());
                vo.setConfirmReused(confirmVo.getReused());
                vo.setMessage("已自动生成记账记录");
            } catch (Exception e) {
                log.warn("BookkeepingAssistantService#parseExpenseAudio 自动保存语音支出记账失败, logId: {}, message: {}",
                        logEntity.getId(), e.getMessage(), e);
                status = BookkeepingAssistantParseStatusEnum.NEED_CONFIRM;
                message = "已生成记账草稿，自动保存失败，请确认后保存";
                ambiguities.add("autoSaveFailed");
                logEntity.setParseStatus(status.name());
                logEntity.setWarningJson(this.buildWarningJson(missingFields, ambiguities));
                bookkeepingVoiceParseLogDao.updateById(logEntity);
                vo.setStatus(status.name());
                vo.setMessage(message);
                vo.setAutoSaveEligible(Boolean.FALSE);
            }
        }
        return vo;
    }

    @Override
    @Transactional
    public BookkeepingAssistantConfirmExpenseVo confirmExpense(BookkeepingAssistantConfirmExpenseDto dto) {
        if (dto == null || dto.getLogId() == null) {
            throw new IwWebException("解析日志ID不能为空");
        }
        if (!RecordCategoryEnum.CONSUME.equals(dto.getRecordCategory())) {
            throw new IwWebException("当前仅支持支出语音记账");
        }

        BookkeepingVoiceParseLogEntity logEntity = bookkeepingVoiceParseLogDao.lambdaQuery()
                .eq(BookkeepingVoiceParseLogEntity::getId, dto.getLogId())
                .eq(BookkeepingVoiceParseLogEntity::getUserId, UserUtils.getUserId())
                .one();
        if (logEntity == null) {
            throw new IwWebException("语音解析日志不存在");
        }

        BookkeepingAssistantConfirmExpenseVo vo = new BookkeepingAssistantConfirmExpenseVo();
        vo.setLogId(logEntity.getId());
        if (BookkeepingAssistantConfirmStatusEnum.CONFIRMED.name().equals(logEntity.getConfirmStatus())
                && logEntity.getConfirmedRecordId() != null) {
            vo.setRecordId(logEntity.getConfirmedRecordId());
            vo.setReused(Boolean.TRUE);
            return vo;
        }

        Integer recordId = bookkeepingRecordsService.add(dto);
        logEntity.setConfirmStatus(BookkeepingAssistantConfirmStatusEnum.CONFIRMED.name());
        logEntity.setConfirmedRecordId(recordId);
        logEntity.setConfirmedDataJson(JSONUtil.toJsonStr(dto));
        logEntity.setConfirmedTime(java.time.LocalDateTime.now());
        bookkeepingVoiceParseLogDao.updateById(logEntity);

        vo.setRecordId(recordId);
        vo.setReused(Boolean.FALSE);
        return vo;
    }

    private boolean isAutoSaveEligible(BookkeepingAssistantParseStatusEnum status,
                                       BigDecimal confidence,
                                       BookkeepingAssistantExpenseDraftVo draftVo,
                                       List<String> missingFields,
                                       List<String> ambiguities) {
        return BookkeepingAssistantParseStatusEnum.READY.equals(status)
                && confidence != null
                && confidence.compareTo(AUTO_SAVE_CONFIDENCE_THRESHOLD) >= 0
                && draftVo != null
                && Objects.equals(draftVo.getRecordCategory(), RecordCategoryEnum.CONSUME.getCode())
                && draftVo.getAmount() != null
                && draftVo.getRecordType() != null
                && StringUtils.isNotBlank(draftVo.getRecordSource())
                && missingFields.isEmpty()
                && ambiguities.isEmpty();
    }

    private BookkeepingAssistantConfirmExpenseDto buildConfirmExpenseDto(Integer logId, BookkeepingAssistantExpenseDraftVo draftVo) {
        BookkeepingAssistantConfirmExpenseDto dto = new BookkeepingAssistantConfirmExpenseDto();
        dto.setLogId(logId);
        dto.setRecordDate(draftVo.getRecordDate());
        dto.setRecordCategory(RecordCategoryEnum.CONSUME);
        dto.setRecordSource(draftVo.getRecordSource());
        dto.setAmount(draftVo.getAmount());
        dto.setRecordType(draftVo.getRecordType());
        dto.setRecordTags(draftVo.getRecordTags());
        dto.setIsExcitationRecord(BoolEnum.FALSE.getCode());
        dto.setIsStatistics(draftVo.getIsStatistics());
        dto.setRecordIcon(draftVo.getRecordIcon());
        dto.setShared(draftVo.getShared());
        return dto;
    }

    private String buildWarningJson(List<String> missingFields, List<String> ambiguities) {
        Map<String, Object> warningMap = new LinkedHashMap<>();
        warningMap.put("missingFields", missingFields);
        warningMap.put("ambiguities", ambiguities);
        return JSONUtil.toJsonStr(warningMap);
    }

    private String resolveParseFailureMessage(String errorMessage) {
        if (StringUtils.containsIgnoreCase(errorMessage, "WebM") && StringUtils.containsIgnoreCase(errorMessage, "转码")) {
            return "语音转码失败，请检查服务端ffmpeg配置";
        }
        if (StringUtils.containsIgnoreCase(errorMessage, "WebM")) {
            return "检测到WebM录音，已尝试服务端转码，请稍后重试";
        }
        if (StringUtils.containsIgnoreCase(errorMessage, "NO_VALID_AUDIO_ERROR")) {
            return "录音内容无效，请重试录音";
        }
        if (StringUtils.isBlank(errorMessage)) {
            return "语音解析失败，请重试";
        }
        return errorMessage;
    }

    private AiStructuredChatDto buildStructuredChatDto(String recognizedText,
                                                       BigDecimal amount,
                                                       List<BookkeepingActionsEntity> actionList,
                                                       List<DictListVo> recordTypeList) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("你是一个记账助手，只做单笔支出记账解析。");
        promptBuilder.append("请基于用户文本，解析金额，并从提供的候选行为和候选分类中选择最合适的结果，输出严格JSON。");
        promptBuilder.append("不要输出markdown，不要补充解释。");
        promptBuilder.append("JSON字段固定为：amount,recordSource,recordType,matchedActionId,ambiguities。");
        promptBuilder.append("amount必须是以元为单位的数字，支持将中文金额转换为数字，最多保留两位小数；如果金额不确定，amount返回null。");
        promptBuilder.append("如果分类不确定，recordType返回null，matchedActionId返回null，ambiguities写原因数组。");
        promptBuilder.append("用户文本：").append(recognizedText).append("。");
        if (amount != null) {
            promptBuilder.append("已规则提取金额：").append(amount).append("。");
        } else {
            promptBuilder.append("规则未提取到阿拉伯数字金额，请重点判断中文金额表达。");
        }
        promptBuilder.append("候选行为：").append(JSONUtil.toJsonStr(actionList.stream().map(t -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", t.getId());
            map.put("recordSource", t.getRecordSource());
            map.put("recordType", t.getRecordType());
            map.put("recordIcon", t.getRecordIcon());
            map.put("recordTags", t.getRecordTags());
            return map;
        }).toList())).append("。");
        promptBuilder.append("候选分类：").append(JSONUtil.toJsonStr(recordTypeList.stream().map(t -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("dictCode", t.getDictCode());
            map.put("dictName", t.getDictName());
            return map;
        }).toList())).append("。");

        AiChatMessageDto systemMessage = new AiChatMessageDto();
        systemMessage.setRole("system");
        systemMessage.setContent("你只返回合法JSON，不输出额外内容。");

        AiChatMessageDto userMessage = new AiChatMessageDto();
        userMessage.setRole("user");
        userMessage.setContent(promptBuilder.toString());

        AiStructuredChatDto dto = new AiStructuredChatDto();
        dto.setMessages(List.of(systemMessage, userMessage));
        dto.setModel("deepseek-chat");
        dto.setMaxTokens(512);
        dto.setTemperature(0.1D);
        return dto;
    }

    private Integer applyAiResult(String aiContent,
                                  List<BookkeepingActionsEntity> actionList,
                                  List<DictListVo> recordTypeList,
                                  BookkeepingAssistantExpenseDraftVo draftVo,
                                  List<String> ambiguities) {
        if (StringUtils.isBlank(aiContent)) {
            ambiguities.add("aiEmptyResult");
            return null;
        }
        try {
            String jsonContent = this.extractJson(aiContent);
            Map<String, Object> map = JSONUtil.toBean(jsonContent, Map.class);
            BigDecimal aiAmount = this.parseAmount(map.get("amount"));
            Integer matchedActionId = this.parseInteger(map.get("matchedActionId"));
            Integer recordType = this.parseInteger(map.get("recordType"));
            String recordSource = map.get("recordSource") == null ? null : map.get("recordSource").toString();

            if (draftVo.getAmount() == null && aiAmount != null) {
                draftVo.setAmount(aiAmount.setScale(2, RoundingMode.HALF_UP));
            }

            if (matchedActionId != null) {
                Optional<BookkeepingActionsEntity> actionOptional = actionList.stream()
                        .filter(t -> Objects.equals(t.getId(), matchedActionId))
                        .findFirst();
                if (actionOptional.isPresent()) {
                    BookkeepingActionsEntity action = actionOptional.get();
                    draftVo.setRecordType(action.getRecordType());
                    draftVo.setRecordSource(StringUtils.defaultIfBlank(recordSource, action.getRecordSource()));
                    draftVo.setRecordIcon(action.getRecordIcon());
                    draftVo.setRecordTags(this.parseTags(action.getRecordTags()));
                    return matchedActionId;
                }
            }

            if (recordType != null && recordTypeList.stream().anyMatch(t -> Objects.equals(t.getDictCode(), recordType))) {
                draftVo.setRecordType(recordType);
            }
            if (StringUtils.isNotBlank(recordSource)) {
                draftVo.setRecordSource(recordSource);
            }
            List<?> aiAmbiguities = map.get("ambiguities") instanceof List<?> list ? list : Collections.emptyList();
            aiAmbiguities.stream().map(String::valueOf).forEach(ambiguities::add);
            return matchedActionId;
        } catch (Exception e) {
            ambiguities.add("aiResultParseFailed");
            return null;
        }
    }

    private String extractJson(String content) {
        int start = content.indexOf("{");
        int end = content.lastIndexOf("}");
        if (start >= 0 && end > start) {
            return content.substring(start, end + 1);
        }
        return content;
    }

    private List<Integer> parseTags(String recordTags) {
        if (StringUtils.isBlank(recordTags)) {
            return Collections.emptyList();
        }
        return Arrays.stream(recordTags.split(","))
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .map(Integer::valueOf)
                .toList();
    }

    private List<BookkeepingActionsEntity> queryExpenseActionList() {
        return bookkeepingActionsDao.lambdaQuery()
                .eq(BookkeepingActionsEntity::getUserId, UserUtils.getUserId())
                .eq(BookkeepingActionsEntity::getRecordCategory, RecordCategoryEnum.CONSUME.getCode())
                .orderByAsc(BookkeepingActionsEntity::getSort)
                .orderByAsc(BookkeepingActionsEntity::getId)
                .list();
    }

    private Integer resolveDefaultShared() {
        FamilySharedSavePolicyVo policyVo = authFamilyGroupClient.querySharedSavePolicy(UserUtils.getUserId());
        if (policyVo == null || policyVo.getCurrentGroupId() == null || policyVo.getCurrentGroupId() <= 0) {
            return BoolEnum.FALSE.getCode();
        }
        if (BoolEnum.TRUE.getCode().equals(policyVo.getForceShared())) {
            return BoolEnum.TRUE.getCode();
        }
        return BoolEnum.TRUE.getCode().equals(policyVo.getDefaultShared()) ? BoolEnum.TRUE.getCode() : BoolEnum.FALSE.getCode();
    }

    private LocalDate resolveRecordDate(String recognizedText) {
        LocalDate now = LocalDate.now();
        if (StringUtils.contains(recognizedText, "昨天")) {
            return now.minusDays(1);
        }
        if (StringUtils.contains(recognizedText, "前天")) {
            return now.minusDays(2);
        }
        Matcher monthDayMatcher = Pattern.compile("(\\d{1,2})月(\\d{1,2})[日号]?").matcher(recognizedText);
        if (monthDayMatcher.find()) {
            int month = Integer.parseInt(monthDayMatcher.group(1));
            int day = Integer.parseInt(monthDayMatcher.group(2));
            return LocalDate.of(now.getYear(), month, day);
        }
        Matcher isoDateMatcher = Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2})").matcher(recognizedText);
        if (isoDateMatcher.find()) {
            return LocalDate.parse(isoDateMatcher.group(1), DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return now;
    }

    private BigDecimal extractAmount(String recognizedText) {
        Matcher matcher = AMOUNT_PATTERN.matcher(recognizedText);
        BigDecimal max = null;
        while (matcher.find()) {
            BigDecimal current = new BigDecimal(matcher.group(1));
            if (max == null || current.compareTo(max) > 0) {
                max = current;
            }
        }
        return max;
    }

    private String resolveFormat(String fileName) {
        if (StringUtils.isBlank(fileName) || !fileName.contains(".")) {
            return "mp3";
        }
        return StringUtils.lowerCase(StringUtils.substringAfterLast(fileName, "."));
    }

    private Integer parseInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (StringUtils.isBlank(value.toString()) || "null".equalsIgnoreCase(value.toString())) {
            return null;
        }
        return Integer.parseInt(value.toString());
    }

    private BigDecimal parseAmount(Object value) {
        if (value == null) {
            return null;
        }
        try {
            BigDecimal amount;
            if (value instanceof Number number) {
                amount = new BigDecimal(number.toString());
            } else {
                String amountText = StringUtils.remove(value.toString(), ",");
                if (StringUtils.isBlank(amountText) || "null".equalsIgnoreCase(amountText)) {
                    return null;
                }
                amount = new BigDecimal(amountText);
            }
            return amount.compareTo(BigDecimal.ZERO) > 0 ? amount : null;
        } catch (Exception e) {
            return null;
        }
    }
}
