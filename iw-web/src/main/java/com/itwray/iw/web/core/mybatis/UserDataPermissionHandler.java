package com.itwray.iw.web.core.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.itwray.iw.web.config.IwDaoProperties;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;

import java.util.Set;

/**
 * 用户数据权限处理器
 *
 * @author wray
 * @since 2024/8/30
 */
@Slf4j
public class UserDataPermissionHandler implements MultiDataPermissionHandler {

    private final IwDaoProperties.DataPermission dataPermission;

    private final Set<String> ignoreUserDataPermissionMethods;

    public UserDataPermissionHandler(IwDaoProperties.DataPermission dataPermission,
                                     Set<String> ignoreUserDataPermissionMethods) {
        this.dataPermission = dataPermission;
        this.ignoreUserDataPermissionMethods = ignoreUserDataPermissionMethods;
    }

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        if (dataPermission.disableTable(table.getName())) {
            // 表示不追加任何条件
            return null;
        }
        if (ignoreUserDataPermissionMethods.contains(mappedStatementId)) {
            return null;
        }

        String whereSql = where.toString();
        // 如果where中已包含user_id查询条件，则不追加 where 条件
        if (whereSql != null && whereSql.contains("user_id")) {
            return null;
        }
        String sqlSegment = "user_id = " + UserUtils.getUserId();
        try {
            return CCJSqlParserUtil.parseCondExpression(sqlSegment);
        } catch (JSQLParserException e) {
            log.error("解析条件表达式失败", e);
            return null;
        }
    }
}
