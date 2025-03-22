package ${package.ServiceImpl};

<#if table.serviceInterface>
import ${package.Service}.${table.serviceName};
</#if>
<#if enabledWebModule>
import com.itwray.iw.web.service.impl.WebServiceImpl;
import ${package.Parent}.model.dto.${addDtoName};
import ${package.Parent}.model.dto.${updateDtoName};
import ${package.Parent}.model.vo.${detailVoName};
import ${package.Parent}.dao.${daoName};
import ${package.Mapper}.${table.mapperName};
import ${package.Entity}.${entity};
import org.springframework.beans.factory.annotation.Autowired;
</#if>
import org.springframework.stereotype.Service;

/**
 * ${table.comment!} 服务实现类
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if enabledWebModule>
public class ${table.serviceImplName} extends WebServiceImpl<${daoName}, ${table.mapperName}, ${entity},
        ${addDtoName}, ${updateDtoName}, ${detailVoName}, Integer> <#if table.serviceInterface> implements ${table.serviceName}</#if> {
<#else>
public class ${table.serviceImplName}<#if table.serviceInterface> implements ${table.serviceName}</#if> {
</#if>
<#if enabledWebModule>

    @Autowired
    public ${table.serviceImplName}(${daoName} baseDao) {
        super(baseDao);
    }
</#if>
}
