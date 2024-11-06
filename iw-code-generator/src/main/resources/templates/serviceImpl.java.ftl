package ${package.ServiceImpl};

<#if table.serviceInterface>
import ${package.Service}.${table.serviceName};
</#if>
import org.springframework.stereotype.Service;

/**
 * ${table.comment!} 服务实现类
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${table.serviceImplName}<#if table.serviceInterface> implements ${table.serviceName}</#if> {

}
