package ${package.Parent}.model.dto;

import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou") && !pkg?starts_with("com.itwray")>
import ${pkg};
    </#if>
</#list>

/**
 * ${table.comment!} 新增DTO
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@Schema(name = "${table.comment!} 新增DTO")
public class ${addDtoName} implements AddDto {

<#list table.fields as field>
  <#if field.comment!?length gt 0>
    @Schema(title = "${field.comment}")
  </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
}
