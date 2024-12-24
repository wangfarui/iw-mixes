package com.itwray.iw.external.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.BaseEntity;
import lombok.*;

/**
 * 外部SMS短信记录表
 *
 * @author wray
 * @since 2024-12-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("external_sms_records")
public class SmsRecordsEntity extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 签名名称
     */
    private String signName;

    /**
     * 模板CODE
     */
    private String templateCode;

    /**
     * 模板参数
     */
    private String templateParam;

    /**
     * 状态(0待发送, 1发送成功, 2发送失败)
     *
     * @see com.itwray.iw.external.model.enums.SmsSendStatusEnum
     */
    private Boolean sendStatus;

}
