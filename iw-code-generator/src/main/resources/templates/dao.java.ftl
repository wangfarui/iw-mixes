package ${package.Parent}.dao;

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import com.itwray.iw.web.dao.BaseDao;
import org.springframework.stereotype.Component;

/**
 * ${table.comment!} DAO
 *
 * @author ${author}
 * @since ${date}
 */
@Component
public class ${daoName} extends BaseDao<${table.mapperName}, ${entity}> {

}
