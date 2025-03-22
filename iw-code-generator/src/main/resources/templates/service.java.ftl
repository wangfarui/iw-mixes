package ${package.Service};
<#if enabledWebModule>

import com.itwray.iw.web.service.WebService;
import ${package.Parent}.model.dto.${addDtoName};
import ${package.Parent}.model.dto.${updateDtoName};
import ${package.Parent}.model.vo.${detailVoName};
</#if>

/**
 * ${table.comment!} 服务接口
 *
 * @author ${author}
 * @since ${date}
 */
<#if enabledWebModule>
public interface ${table.serviceName} extends WebService<${addDtoName}, ${updateDtoName}, ${detailVoName}, Integer> {
<#else>
public interface ${table.serviceName} {
</#if>

}
