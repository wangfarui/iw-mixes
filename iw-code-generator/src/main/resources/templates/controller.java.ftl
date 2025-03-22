package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if enabledWebModule>
import ${package.Parent}.model.dto.${addDtoName};
import ${package.Parent}.model.dto.${updateDtoName};
import ${package.Parent}.model.vo.${detailVoName};
import ${package.Service}.${table.serviceName};
import com.itwray.iw.web.controller.WebController;
</#if>
import io.swagger.v3.oas.annotations.tags.Tag;
<#if enabledWebModule>
import org.springframework.beans.factory.annotation.Autowired;
</#if>
import org.springframework.validation.annotation.Validated;

/**
 * ${table.comment!} 接口控制层
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${actualTableName}</#if>")
@Validated
@Tag(name = "${table.comment!}接口")
<#if enabledWebModule>
public class ${table.controllerName} extends WebController<${table.serviceName},
        ${addDtoName}, ${updateDtoName}, ${detailVoName}, Integer>  {
<#else>
public class ${table.controllerName} {
</#if>
<#if enabledWebModule>

    @Autowired
    public ${table.controllerName}(${table.serviceName} webService) {
        super(webService);
    }
</#if>
}
