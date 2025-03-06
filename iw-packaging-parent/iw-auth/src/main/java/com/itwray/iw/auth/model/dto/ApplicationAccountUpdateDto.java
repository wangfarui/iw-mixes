package com.itwray.iw.auth.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用账号信息 更新DTO
 *
 * @author wray
 * @since 2025/3/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplicationAccountUpdateDto extends ApplicationAccountAddDto implements UpdateDto {

    /**
     * id
     */
    private Integer id;
}
