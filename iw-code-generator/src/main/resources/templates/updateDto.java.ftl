package ${package.Parent}.model.dto;

import com.itwray.iw.web.model.dto.UpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ${table.comment!} 更新DTO
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@Schema(name = "${table.comment!} 更新DTO")
public class ${updateDtoName} implements UpdateDto {

    @Schema(title = "id")
    private Integer id;
}
