package com.itwray.iw.web.core.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.itwray.iw.web.config.IwDaoProperties;
import com.itwray.iw.web.utils.ApplicationContextHolder;
import com.itwray.iw.web.utils.UserCurrentGroupUtils;
import com.itwray.iw.web.utils.UserSharedQueryUtils;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 用户数据权限处理器
 *
 * @author wray
 * @since 2024/8/30
 */
@Slf4j
public class UserDataPermissionHandler implements MultiDataPermissionHandler {

    private static final Pattern USER_ID_PATTERN = Pattern.compile("(?i)\\buser_id\\b");

    private final IwDaoProperties.DataPermission dataPermission;

    private final Set<String> ignoreUserDataPermissionMethods;

    private final Map<String, SqlCommandType> sqlCommandTypeCache = new ConcurrentHashMap<>();

    private volatile UserCurrentGroupProvider userCurrentGroupProvider;

    private volatile boolean userCurrentGroupProviderLoaded;

    private volatile SqlSessionFactory sqlSessionFactory;

    private volatile boolean sqlSessionFactoryLoaded;

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
        // 当前线程关闭了用户数据权限
        if (!UserUtils.getUserDataPermission()) {
            return null;
        }
        if (ignoreUserDataPermissionMethods.contains(mappedStatementId)) {
            return null;
        }

        String whereSql = where == null ? null : where.toString();
        SqlCommandType sqlCommandType = this.getSqlCommandType(mappedStatementId);

        // 共享查询：仅对指定表 + 查询语句 + 共享查询开关开启时生效
        if (SqlCommandType.SELECT.equals(sqlCommandType)
                && dataPermission.enableShareScopeTable(table.getName())
                && UserSharedQueryUtils.getUserSharedQuery()
                && !UserSharedQueryUtils.getUserSharedQueryOnlyMyself()) {
            Expression sharedExpression = this.buildSharedQueryExpression(whereSql);
            if (sharedExpression != null) {
                return sharedExpression;
            }
        }

        // 默认规则：仅本人数据
        if (this.containsUserIdCondition(whereSql)) {
            return null;
        }

        return this.parseExpression("user_id = " + UserUtils.getUserId());
    }

    private Expression buildSharedQueryExpression(String whereSql) {
        // 如果SQL自身已经显式携带user_id条件，则尊重业务SQL，避免重复拼接造成冲突
        if (this.containsUserIdCondition(whereSql)) {
            return null;
        }

        Integer userId = UserUtils.getUserId();
        Integer currentGroupId = this.getCurrentGroupId(userId);
        StringBuilder sqlSegment = new StringBuilder("user_id = ").append(userId);
        if (currentGroupId != null && currentGroupId > 0) {
            sqlSegment.append(" OR (group_id = ")
                    .append(currentGroupId)
                    .append(" AND share_state = 1)");
        }
        return this.parseExpression("(" + sqlSegment + ")");
    }

    private boolean containsUserIdCondition(String whereSql) {
        return whereSql != null && USER_ID_PATTERN.matcher(whereSql).find();
    }

    private Expression parseExpression(String sqlSegment) {
        try {
            return CCJSqlParserUtil.parseCondExpression(sqlSegment);
        } catch (JSQLParserException e) {
            log.error("解析条件表达式失败", e);
            return null;
        }
    }

    private Integer getCurrentGroupId(Integer userId) {
        Integer currentGroupId = UserCurrentGroupUtils.getCurrentGroupId();
        if (currentGroupId != null) {
            return currentGroupId;
        }

        currentGroupId = 0;
        UserCurrentGroupProvider provider = this.getUserCurrentGroupProvider();
        if (provider != null) {
            try {
                Integer groupId = provider.queryCurrentGroupId(userId);
                currentGroupId = groupId == null ? 0 : groupId;
            } catch (Exception e) {
                log.error("查询用户当前家庭组ID失败, userId: {}", userId, e);
            }
        }
        UserCurrentGroupUtils.setCurrentGroupId(currentGroupId);
        return currentGroupId;
    }

    private UserCurrentGroupProvider getUserCurrentGroupProvider() {
        if (!userCurrentGroupProviderLoaded) {
            synchronized (this) {
                if (!userCurrentGroupProviderLoaded) {
                    userCurrentGroupProvider = ApplicationContextHolder
                            .getBeanProvider(UserCurrentGroupProvider.class)
                            .getIfAvailable();
                    userCurrentGroupProviderLoaded = true;
                }
            }
        }
        return userCurrentGroupProvider;
    }

    private SqlCommandType getSqlCommandType(String mappedStatementId) {
        return sqlCommandTypeCache.computeIfAbsent(mappedStatementId, this::resolveSqlCommandType);
    }

    private SqlCommandType resolveSqlCommandType(String mappedStatementId) {
        try {
            SqlSessionFactory sessionFactory = this.getSqlSessionFactory();
            if (sessionFactory == null) {
                return SqlCommandType.UNKNOWN;
            }
            MappedStatement mappedStatement = sessionFactory.getConfiguration().getMappedStatement(mappedStatementId);
            return mappedStatement.getSqlCommandType();
        } catch (Exception e) {
            log.error("获取SQL命令类型失败, mappedStatementId: {}", mappedStatementId, e);
            return SqlCommandType.UNKNOWN;
        }
    }

    private SqlSessionFactory getSqlSessionFactory() {
        if (!sqlSessionFactoryLoaded) {
            synchronized (this) {
                if (!sqlSessionFactoryLoaded) {
                    sqlSessionFactory = ApplicationContextHolder
                            .getBeanProvider(SqlSessionFactory.class)
                            .getIfAvailable();
                    sqlSessionFactoryLoaded = true;
                }
            }
        }
        return sqlSessionFactory;
    }
}
