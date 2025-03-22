package ${package.Parent}.model.vo;

import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou") && !pkg?starts_with("com.itwray")>
import ${pkg};
    </#if>
</#list>

/**
 * ${table.comment!} 详情VO
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@Schema(name = "${table.comment!} 详情VO")
public class ${detailVoName} implements DetailVo {

<#list table.fields as field>
  <#if field.comment!?length gt 0>
    @Schema(title = "${field.comment}")
  </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
}
