package com.itwray.iw.eat.model.vo;

import com.itwray.iw.web.model.FileVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件记录VO
 *
 * @author wray
 * @since 2024/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileRecordVo extends FileVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 文件路径
     */
    private String fileUri;

    /**
     * 文件前缀
     */
    private String filePrefix;

    /**
     * 文件后缀
     */
    private String fileSuffix;

}
