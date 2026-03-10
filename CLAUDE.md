# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**iw-mixes** 是一个基于 Spring Boot 的微服务架构后端项目，旨在用一个项目支持 IW 系统的所有服务端功能，围绕 ToC 用户提供登录、记账、食谱、任务、积分、信息管理等功能服务。

## Technology Stack

- **Java 17** - 编程语言
- **Spring Boot 3.2.12** - 应用框架
- **Spring Cloud 4.1.5** - 微服务框架
- **Spring Cloud Alibaba 2023.0.3.3** - 阿里云微服务组件
- **Nacos 2.3.2** - 服务注册与配置中心
- **MySQL 8.x** - 关系型数据库
- **Redis 7.x** - 缓存和分布式锁
- **RocketMQ 5.3.0** - 消息队列
- **MyBatis-Plus 3.5.5** - ORM 框架
- **Hutool 5.8.26** - Java 工具库
- **SpringDoc 2.5.0** - OpenAPI 文档生成
- **Aliyun OSS 3.17.2** - 对象存储

## Build & Run Commands

### 构建项目

```bash
# 构建整个项目（跳过测试）
mvn clean package -DskipTests

# 构建指定模块
cd iw-packaging-parent/iw-auth
mvn clean package -DskipTests

# 只编译不打包
mvn clean compile
```

### 运行服务

```bash
# 方式1：使用 Maven 插件运行（开发环境）
cd iw-packaging-parent/iw-auth
mvn spring-boot:run

# 方式2：运行打包后的 JAR（生产环境）
java -jar iw-packaging-parent/iw-auth/target/iw-auth-0.2.1.jar

# 指定配置文件
java -jar iw-auth-0.2.1.jar --spring.profiles.active=prod
```

### 代码生成

```bash
# 运行 MyBatis-Plus 代码生成器
cd iw-code-generator
mvn spring-boot:run

# 根据提示输入：
# - 数据库表名
# - 模块名称
# - 包路径
# 生成的代码包括：Entity、Mapper、Service、Controller
```

### 测试

```bash
# 运行所有测试
mvn test

# 运行指定模块的测试
cd iw-packaging-parent/iw-auth
mvn test
```

## Project Structure

```
iw-mixes/
├── iw-common/                      # 公共基础模块
│   └── src/main/java/com/itwray/iw/common/
│       ├── ApiCode.java           # API 状态码接口
│       ├── GeneralResponse.java   # 统一响应对象
│       ├── IwException.java       # 自定义异常基类
│       ├── constants/             # 常量定义
│       │   ├── BoolEnum.java     # 布尔枚举
│       │   ├── EnableEnum.java   # 启用/禁用枚举
│       │   ├── GeneralApiCode.java  # 通用状态码
│       │   └── RequestHeaderConstants.java  # 请求头常量
│       └── utils/                 # 工具类
│           ├── AESUtils.java     # AES 加密工具
│           ├── DateUtils.java    # 日期工具
│           ├── NumberUtils.java  # 数字工具
│           └── ExceptionUtils.java  # 异常工具
│
├── iw-web/                         # Web 服务基础模块
│   └── src/main/java/com/itwray/iw/web/
│       ├── SwaggerConfig.java     # Swagger 配置
│       ├── annotation/            # 自定义注解
│       ├── client/                # Feign 客户端配置
│       ├── config/                # 配置类
│       ├── controller/            # 基础控制器
│       ├── core/                  # 核心组件
│       │   ├── dingtalk/         # 钉钉集成
│       │   ├── feign/            # Feign 配置
│       │   ├── mybatis/          # MyBatis 配置
│       │   ├── tracing/          # 链路追踪
│       │   └── webmvc/           # WebMVC 配置
│       ├── dao/                   # 数据访问层基类
│       ├── exception/             # 异常处理
│       │   ├── AuthorizedException.java  # 授权异常
│       │   ├── BusinessException.java    # 业务异常
│       │   ├── FeignClientException.java # Feign 异常
│       │   └── IwWebException.java       # Web 异常
│       ├── mapper/                # Mapper 基类
│       ├── model/                 # 模型基类
│       ├── service/               # Service 基类
│       └── utils/                 # Web 工具类
│
├── iw-feign-client/                # Feign 客户端模块
│   ├── iw-auth-client/            # 认证服务客户端
│   ├── iw-bookkeeping-client/     # 记账服务客户端
│   ├── iw-eat-client/             # 餐饮服务客户端
│   ├── iw-points-client/          # 积分服务客户端
│   └── iw-external-client/        # 外部服务客户端
│
├── iw-starter/                     # 自定义 Starter
│   ├── iw-redis-starter/          # Redis Starter
│   └── iw-rocketmq-starter/       # RocketMQ Starter
│
├── iw-packaging-parent/            # 微服务打包父模块
│   ├── iw-gateway/                # API 网关 (18000)
│   ├── iw-auth/                   # 认证服务 (18001)
│   │   └── src/main/java/com/itwray/iw/auth/
│   │       ├── controller/        # 控制器层
│   │       ├── service/           # 业务逻辑层
│   │       ├── dao/               # 数据访问层
│   │       ├── mapper/            # MyBatis Mapper
│   │       ├── model/             # 数据模型
│   │       └── job/               # 定时任务
│   ├── iw-note/                   # 笔记服务 (18002) - 暂未开发
│   ├── iw-eat/                    # 餐饮服务 (18003) - 合并至 iw-bookkeeping
│   ├── iw-bookkeeping/            # 记账服务 (18004)
│   ├── iw-points/                 # 积分服务 (18005) - 合并至 iw-bookkeeping
│   └── iw-external/               # 外部服务 (18006)
│
├── iw-code-generator/              # MyBatis-Plus 代码生成器
├── iw-oauth2-authorization-server/ # OAuth2 授权服务 (18101) - 暂未启用
└── scripts/                        # 数据库脚本
    └── database-init.sql          # 数据库初始化脚本
```

## Architecture & Patterns

### 微服务架构

**服务列表：**

| 服务名称 | 端口 | 说明 | 状态 |
|---------|------|------|------|
| iw-gateway | 18000 | API 网关，统一入口 | 运行中 |
| iw-auth | 18001 | 用户认证、授权、基础服务 | 运行中 |
| iw-note | 18002 | 笔记服务 | 暂未开发 |
| iw-eat | 18003 | 餐饮服务 | 合并至 iw-bookkeeping |
| iw-bookkeeping | 18004 | 记账服务 | 运行中 |
| iw-points | 18005 | 积分服务 | 合并至 iw-bookkeeping |
| iw-external | 18006 | 外部服务（需要外网） | 运行中 |
| iw-oauth2-authorization-server | 18101 | OAuth2 授权服务 | 暂未启用 |

**服务发现：**
- 使用 Nacos 作为服务注册中心
- 服务启动时自动注册到 Nacos
- 通过 Spring Cloud LoadBalancer 实现客户端负载均衡

**服务通信：**
- 使用 OpenFeign 进行服务间调用
- Feign 客户端定义在 `iw-feign-client` 模块
- 支持链路追踪（Micrometer Tracing）

### 分层架构

**标准分层结构：**
```
Controller (控制器层)
    ↓
Service (业务逻辑层)
    ↓
Dao (数据访问层)
    ↓
Mapper (MyBatis 映射层)
    ↓
Database (数据库)
```

**包结构规范：**
```
com.itwray.iw.{module}/
├── controller/          # REST API 控制器
├── service/            # 业务逻辑
│   └── impl/          # 业务逻辑实现
├── dao/               # 数据访问对象
├── mapper/            # MyBatis Mapper 接口
├── model/             # 数据模型
│   ├── entity/       # 数据库实体
│   ├── dto/          # 数据传输对象
│   ├── vo/           # 视图对象
│   └── enums/        # 枚举类
├── job/               # 定时任务
└── utils/             # 工具类
```

### 统一响应格式

所有 API 返回统一的响应格式：

```java
{
    "code": 200,           // 业务状态码
    "message": "success",  // 响应消息
    "data": {}            // 响应数据
}
```

**使用方式：**
```java
// Controller 中直接返回数据，框架自动包装
@GetMapping("/detail")
public UserVo getDetail(@RequestParam Integer id) {
    return userService.getDetail(id);
}

// 手动构造响应
return GeneralResponse.success(data);
return GeneralResponse.error(ApiCode.PARAM_ERROR, "参数错误");
```

### 异常处理

**异常层次：**
- `IwException` - 自定义异常基类
  - `BusinessException` - 业务异常（用于业务逻辑错误）
  - `AuthorizedException` - 授权异常（用于权限不足）
  - `FeignClientException` - Feign 调用异常
  - `IwWebException` - Web 层异常

**使用方式：**
```java
// 抛出业务异常
throw new BusinessException(ApiCode.USER_NOT_FOUND, "用户不存在");

// 全局异常处理器会自动捕获并返回统一格式
```

### 数据库设计

**命名规范：**
- 表名：小写字母 + 下划线，如 `auth_user`、`bookkeeping_record`
- 字段名：小写字母 + 下划线，如 `user_name`、`create_time`
- 主键：统一使用 `id`，类型优先使用 `int`，必要时使用 `bigint`

**通用字段：**
```sql
id              INT/BIGINT      主键
create_time     DATETIME        创建时间
update_time     DATETIME        更新时间
create_by       INT             创建人ID
update_by       INT             更新人ID
deleted         TINYINT         逻辑删除标记 (0-未删除, 1-已删除)
```

**MyBatis-Plus 配置：**
- 自动填充 `create_time`、`update_time`
- 逻辑删除字段 `deleted`
- 分页插件自动配置

### 数据字典模式

**存储格式：**
- 所有字典关系（一对一、一对多、多对多）统一采用 `<id:name>` 格式
- 示例：`"1:管理员,2:普通用户"`

**业务判断：**
- 当字典具有业务逻辑时，使用枚举 + 表 `code` 字段
- 前后端共享枚举定义

**使用示例：**
```java
// 字典类型枚举
public enum DictTypeEnum {
    AUTH_APPLICATION_ACCOUNT_TYPE(2010, "应用账号-应用类型"),
    EAT_MEAL_TIME(3002, "餐饮-用餐时间"),
    BOOKKEEPING_RECORD_TYPE(4002, "记账-记录分类");

    private final Integer code;
    private final String name;
}
```

### Redis 缓存规则

**强制要求：**
- 所有 Redis key 必须设置过期时间
- 避免因业务变更导致 key 无意义且不过期

**命名规范：**
```
{module}:{business}:{key}
示例：auth:user:token:123456
     bookkeeping:record:daily:20240301
```

### 配置管理

**配置优先级：**
1. Nacos 配置中心（共享配置）
2. `application-{profile}.yml`（环境特定配置）
3. `application.yml`（默认配置）

**配置文件结构：**
```yaml
# application.yml - 基础配置
server:
  port: 18001

spring:
  application:
    name: iw-auth-service
  profiles:
    default: dev
  cloud:
    nacos:
      server-addr: ${SPRING_CLOUD_NACOS_SERVER_ADDR:localhost:8848}
      config:
        file-extension: yaml
      discovery:
        enabled: true

# application-dev.yml - 开发环境配置
spring:
  cloud:
    nacos:
      discovery:
        ip: 127.0.0.1
```

**环境变量：**
- `SPRING_CLOUD_NACOS_SERVER_ADDR` - Nacos 服务器地址
- `SPRING_CLOUD_NACOS_USERNAME` - Nacos 用户名
- `SPRING_CLOUD_NACOS_PASSWORD` - Nacos 密码

## Coding Conventions

### 类命名规范

**核心原则：Entity、Mapper、Dao、Service、Controller 类名必须与数据库表名保持一致**

**表名到类名的映射规则：**
- 数据库表名：`auth_family_group`
- Entity：`AuthFamilyGroup`
- Mapper：`AuthFamilyGroupMapper`
- Dao：`AuthFamilyGroupDao`
- Service：`AuthFamilyGroupService`
- ServiceImpl：`AuthFamilyGroupServiceImpl`
- Controller：`AuthFamilyGroupController`

**DTO、VO、Utils、Enums 类名使用业务含义命名：**
- DTO：`FamilyGroupCreateDto`、`FamilyInviteGenerateDto`（不加表名前缀）
- VO：`FamilyGroupVo`、`FamilyMemberVo`（不加表名前缀）
- Utils：`FamilyGroupUtils`（不加表名前缀）
- Enums：`FamilyGroupStatusEnum`、`FamilyMemberRoleEnum`（使用业务字段实际意义）

**示例对比：**

```java
// ✅ 正确示例
// 表名：auth_family_group
public class AuthFamilyGroup { }                    // Entity
public interface AuthFamilyGroupMapper { }          // Mapper
public class AuthFamilyGroupDao { }                 // Dao
public interface AuthFamilyGroupService { }         // Service
public class AuthFamilyGroupServiceImpl { }         // ServiceImpl
public class AuthFamilyGroupController { }          // Controller

// DTO/VO/Utils 使用业务含义
public class FamilyGroupCreateDto { }               // DTO
public class FamilyGroupVo { }                      // VO
public class FamilyGroupUtils { }                   // Utils
public enum FamilyGroupStatusEnum { }               // Enum

// ❌ 错误示例
public class FamilyGroup { }                        // 缺少表名前缀
public class AuthFamilyGroupCreateDto { }           // DTO 不应有表名前缀
public class AuthFamilyGroupUtils { }               // Utils 不应有表名前缀
```

### 枚举类规范

**核心原则：根据枚举用途选择实现不同的接口**

**1. 业务常量枚举（BusinessConstantEnum）**

适用场景：
- 枚举对应实体字段
- 字段使用 Integer 类型存储
- 需要 MyBatis-Plus 自动映射

实现方式：
```java
@Getter
@AllArgsConstructor
public enum FamilyMemberRoleEnum implements BusinessConstantEnum {
    OWNER(1, "群主"),
    MEMBER(2, "成员");

    private final Integer code;
    private final String name;
}
```

特点：
- 实现 `BusinessConstantEnum` 接口
- 继承 MyBatis-Plus 的 `IEnum<Integer>` 接口
- 自动实现实体枚举映射
- 使用 `getCode()` 方法获取值
- JSON 序列化时自动使用 code 值

**2. 通用常量枚举（ConstantEnum）**

适用场景：
- 不对应实体字段的枚举
- 用于业务逻辑判断
- 包含额外业务属性

实现方式：
```java
@Getter
public enum VerificationCodeActionEnum implements ConstantEnum {
    PHONE_EDIT_PASSWORD(1, "修改密码", AuthRedisKeyEnum.EDIT_PASSWORD_KEY),
    USER_LOGIN_REGISTER(3, "用户登录/注册", AuthRedisKeyEnum.USER_LOGIN_PHONE_VERIFY_KEY);

    private final Integer code;
    private final String name;
    private final RedisKeyManager keyManager;  // 额外业务属性

    VerificationCodeActionEnum(Integer code, String name, RedisKeyManager keyManager) {
        this.code = code;
        this.name = name;
        this.keyManager = keyManager;
    }
}
```

特点：
- 实现 `ConstantEnum` 接口
- 可以包含额外的业务属性
- 使用 `getCode()` 方法获取值
- 不自动映射到实体字段

**示例对比：**

```java
// ✅ 正确示例 - 业务常量枚举（对应实体字段）
@Getter
@AllArgsConstructor
public enum TaskStatusEnum implements BusinessConstantEnum {
    WAIT(0, "待完成"),
    DONE(1, "已完成");

    private final Integer code;
    private final String name;
}

// Entity 中使用
@TableName("task")
public class TaskEntity {
    private TaskStatusEnum status;  // MyBatis-Plus 自动映射
}

// ✅ 正确示例 - 通用常量枚举（不对应实体字段）
@Getter
public enum VerificationCodeActionEnum implements ConstantEnum {
    PHONE_EDIT_PASSWORD(1, "修改密码", AuthRedisKeyEnum.EDIT_PASSWORD_KEY);

    private final Integer code;
    private final String name;
    private final RedisKeyManager keyManager;

    VerificationCodeActionEnum(Integer code, String name, RedisKeyManager keyManager) {
        this.code = code;
        this.name = name;
        this.keyManager = keyManager;
    }
}

// ❌ 错误示例
public enum FamilyMemberRoleEnum implements ConstantEnum {  // 应该实现 BusinessConstantEnum
    OWNER(1, "群主");
    private final Integer type;  // 应该使用 code
    private final String name;
}
```

**注意事项：**
- 所有枚举统一使用 `code` 字段名（不要使用 `type`、`value` 等）
- 业务常量枚举必须使用 `@AllArgsConstructor` 简化构造器
- 通用常量枚举需要显式定义构造器（因为有额外属性）
- 获取枚举值统一使用 `getCode()` 方法

### DTO/VO 中使用业务枚举

**核心原则：DTO/VO 中对应实体字段的业务枚举，直接使用枚举类型而非 Integer**

**适用场景：**
- DTO/VO 字段对应 Entity 中实现了 `BusinessConstantEnum` 的枚举字段
- 需要前后端传递枚举值

**实现方式：**

```java
// ✅ 正确示例 - VO 中使用枚举类型
@Data
@Schema(name = "网站导航记录分页VO")
public class WebsiteNavigationPageVo {
    @Schema(title = "网站状态(1在线 2离线)")
    private WebsiteNavigationStatusEnum status;  // 使用枚举类型
}

// ✅ 正确示例 - DTO 中使用枚举类型
@Data
@Schema(name = "网站导航记录 新增DTO")
public class WebsiteNavigationAddDto implements AddDto {
    @Schema(title = "网站状态(1在线 2离线)")
    private WebsiteNavigationStatusEnum status;  // 使用枚举类型
}

// ✅ 正确示例 - 家庭成员 VO
@Data
@Schema(name = "家庭成员 VO")
public class FamilyMemberVo {
    @Schema(title = "角色 (1-群主, 2-成员)")
    private FamilyMemberRoleEnum role;  // 使用枚举类型

    @Schema(title = "状态 (1-正常, 2-已退出, 3-已移除)")
    private FamilyMemberStatusEnum status;  // 使用枚举类型
}

// ❌ 错误示例 - 使用 Integer 类型
@Data
public class FamilyMemberVo {
    private Integer role;    // 应该使用 FamilyMemberRoleEnum
    private Integer status;  // 应该使用 FamilyMemberStatusEnum
}
```

**Service 层处理：**

当需要手动构建 VO 时，使用 `ConstantEnumUtil.findByCode()` 进行转换：

```java
// Entity 中的 role 是 Integer 类型
Map<Integer, FamilyMemberRoleEnum> myRoleMap = memberList.stream()
    .collect(Collectors.toMap(
        AuthFamilyMemberEntity::getGroupId,
        member -> ConstantEnumUtil.findByCode(FamilyMemberRoleEnum.class, member.getRole())
    ));

// 设置到 VO
vo.setMyRole(myRoleMap.get(group.getId()));
```

**优势：**
- 前端接收到的是枚举的 code 值（Integer），符合 API 规范
- 类型安全，避免传递无效的枚举值
- Swagger 文档自动显示枚举可选值
- JSON 序列化时自动使用 code 值（通过 `@JsonValue` 注解）

**注意事项：**
- Entity 中的枚举字段会被 MyBatis-Plus 自动映射（Integer ↔ Enum）
- DTO/VO 中使用枚举类型，Jackson 会自动序列化为 code 值
- 前端传递 Integer 值时，Jackson 会自动反序列化为枚举对象
- 使用 `BeanUtil.copyProperties()` 时，枚举字段需要手动转换

### Entity 中使用业务枚举

**核心原则：Entity 中对应 BusinessConstantEnum 的字段，直接使用枚举类型而非 Integer**

**适用场景：**
- Entity 字段对应实现了 `BusinessConstantEnum` 的枚举
- 需要 MyBatis-Plus 自动进行数据库 Integer 值与枚举对象的映射

**实现方式：**

```java
// ✅ 正确示例 - Entity 中使用枚举类型
@TableName("auth_family_invite")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthFamilyInviteEntity extends BaseEntity<Integer> {
    /**
     * 状态 (1-待使用, 2-已使用, 4-已过期)
     */
    private FamilyInviteStatusEnum status;  // 使用枚举类型
}

@TableName("auth_family_member")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthFamilyMemberEntity extends BaseEntity<Integer> {
    /**
     * 角色 (1-群主, 2-成员)
     */
    private FamilyMemberRoleEnum role;  // 使用枚举类型

    /**
     * 状态 (1-正常, 2-已退出, 3-已移除)
     */
    private FamilyMemberStatusEnum status;  // 使用枚举类型
}

// ❌ 错误示例 - 使用 Integer 类型
@TableName("auth_family_invite")
public class AuthFamilyInviteEntity extends BaseEntity<Integer> {
    private Integer status;  // 应该使用 FamilyInviteStatusEnum
}
```

**Service 层使用：**

```java
// ✅ 直接使用枚举对象进行赋值
memberEntity.setRole(FamilyMemberRoleEnum.OWNER);
memberEntity.setStatus(FamilyMemberStatusEnum.NORMAL);

// ✅ 直接使用枚举对象进行查询条件
familyMemberDao.lambdaQuery()
    .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
    .list();

// ✅ 直接使用枚举对象进行更新
familyMemberDao.lambdaUpdate()
    .eq(AuthFamilyMemberEntity::getId, id)
    .set(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.QUIT)
    .update();

// ✅ 直接使用枚举对象进行比较
if (FamilyMemberRoleEnum.OWNER.equals(memberEntity.getRole())) {
    // 业务逻辑
}

// ❌ 错误示例 - 使用 getCode()
memberEntity.setRole(FamilyMemberRoleEnum.OWNER.getCode());  // 不需要 getCode()
if (FamilyMemberRoleEnum.OWNER.getCode().equals(memberEntity.getRole())) {  // 不需要 getCode()
    // 业务逻辑
}
```

**优势：**
- MyBatis-Plus 自动处理数据库 Integer 值与枚举对象的映射
- 类型安全，编译期检查，避免使用无效的枚举值
- 代码更简洁，不需要手动调用 `getCode()` 或 `ConstantEnumUtil.findByCode()`
- 查询条件、更新操作、比较判断都可以直接使用枚举对象

**MyBatis-Plus 自动映射原理：**
- `BusinessConstantEnum` 继承了 `IEnum<Integer>` 接口
- MyBatis-Plus 会自动调用 `getCode()` 方法将枚举转换为 Integer 存入数据库
- 从数据库查询时，MyBatis-Plus 会自动将 Integer 值转换为对应的枚举对象

**注意事项：**
- 只有实现了 `BusinessConstantEnum` 的枚举才能在 Entity 中使用
- 通用常量枚举（实现 `ConstantEnum`）不能在 Entity 中使用，因为没有实现 `IEnum` 接口
- 确保枚举的 `code` 值与数据库字段值一致

### CRUD 命名规范

- **新增**：`add` 或 `save`
  ```java
  public void addUser(UserDto dto)
  ```

- **编辑**：`update` 或 `modify`
  ```java
  public void updateUser(UserDto dto)
  ```

- **删除**：`delete` 或 `remove`
  ```java
  public void deleteUser(Integer id)
  ```

- **详情**：`detail` 或 `get`
  ```java
  public UserVo detail(Integer id)
  ```

- **分页列表**：`page`
  ```java
  public IPage<UserVo> page(UserQueryDto dto)
  ```

- **全部列表**：`list`
  ```java
  public List<UserVo> list(UserQueryDto dto)
  ```

### ID 类型选择

- 优先使用 `Integer` (int)
- 数据量超过 21 亿时使用 `Long` (bigint)
- 避免过度设计

### 包路径规范

- 所有模块的包路径以 `com.itwray.iw` 开头
- Feign Client 模块的包路径与对应服务保持一致
- 确保相同路径下类名不重复

### Controller 接口路径规范

**核心原则：类级别 @RequestMapping 使用 `/` 分割多个单词，方法级别根据业务语义灵活选择**

**类级别路径（@RequestMapping）：**
- 多个单词使用 `/` 分割
- 采用小写字母
- 体现资源的层级关系

**方法级别路径（@GetMapping/@PostMapping 等）：**
- 根据业务语义灵活选择
- 单个动词或名词：直接使用（如 `/page`、`/join`、`/quit`）
- 多个单词组合：使用驼峰命名（如 `/myGroup`、`/viewPassword`、`/memberList`）

**示例对比：**

```java
// ✅ 正确示例 - 类路径使用 / 分割
@RestController
@RequestMapping("/application/account")  // 多个单词用 / 分割
public class BaseApplicationAccountController {

    @PostMapping("/page")              // 单个单词
    public PageVo<ApplicationAccountPageVo> page() { }

    @GetMapping("/viewPassword")       // 多个单词用驼峰
    public String viewPassword() { }

    @PostMapping("/refreshPassword")   // 多个单词用驼峰
    public void refreshPassword() { }
}

// ✅ 正确示例 - 网站导航
@RestController
@RequestMapping("/website/navigation")  // 多个单词用 / 分割
public class BaseWebsiteNavigationController {

    @PostMapping("/page")
    public PageVo<WebsiteNavigationPageVo> page() { }
}

// ✅ 正确示例 - 家庭组
@RestController
@RequestMapping("/family/group")  // 多个单词用 / 分割
public class AuthFamilyGroupController {

    @GetMapping("/myGroup")           // 多个单词用驼峰
    public FamilyGroupDetailVo myGroup() { }

    @PostMapping("/generateInvite")   // 多个单词用驼峰
    public FamilyInviteVo generateInvite() { }

    @GetMapping("/inviteList")        // 多个单词用驼峰
    public List<FamilyInviteVo> inviteList() { }

    @PostMapping("/join")             // 单个单词
    public void join() { }

    @PostMapping("/removeMember")     // 多个单词用驼峰
    public void removeMember() { }
}

// ❌ 错误示例 - 类路径使用 - 分割
@RestController
@RequestMapping("/family-group")  // 应该使用 /family/group
public class AuthFamilyGroupController { }

@RestController
@RequestMapping("/application-account")  // 应该使用 /application/account
public class BaseApplicationAccountController { }
```

**最终 API 路径示例：**
```
GET  /application/account/page
GET  /application/account/viewPassword
POST /application/account/refreshPassword

GET  /website/navigation/page

GET  /family/group/myGroup
POST /family/group/generateInvite
GET  /family/group/inviteList
POST /family/group/join
POST /family/group/removeMember
```

**注意事项：**
- 类路径必须使用 `/` 分割，保持统一规范
- 方法路径根据业务语义选择，优先使用驼峰命名
- 避免在方法路径中使用 `-` 分割符
- 保持项目内风格一致

### Web 通用封装模式（推荐）

**适用场景：**
- 标准的 CRUD 业务功能（新增、修改、删除、详情）
- 需要快速开发的业务模块
- 业务逻辑相对简单的功能

**核心组件：**

1. **WebService 接口** - 定义通用 CRUD 方法
   ```java
   public interface WebService<A extends AddDto, U extends UpdateDto, V extends DetailVo, ID extends Serializable> {
       ID add(A dto);      // 新增
       void update(U dto); // 修改
       void delete(ID id); // 删除
       V detail(ID id);    // 详情
   }
   ```

2. **WebServiceImpl 抽象类** - 实现通用 CRUD 逻辑
   - 自动处理 DTO 到 Entity 的转换
   - 自动处理 Entity 到 VO 的转换
   - 提供事务支持

3. **WebController 抽象类** - 提供通用 REST API
   - `POST /add` - 新增
   - `PUT /update` - 修改
   - `DELETE /delete` - 删除
   - `GET /detail` - 详情

**开发步骤：**

**步骤 1：定义 DTO**
```java
// AddDto - 新增数据传输对象
@Data
@Schema(name = "应用账号信息 新增DTO")
public class ApplicationAccountAddDto implements AddDto {
    @Schema(title = "应用名称")
    @Length(max = 32, message = "应用名称不能超过32字符")
    private String name;

    @Schema(title = "账号")
    private String account;

    // 其他字段...
}

// UpdateDto - 修改数据传输对象（继承 AddDto）
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "应用账号信息 更新DTO")
public class ApplicationAccountUpdateDto extends ApplicationAccountAddDto implements UpdateDto {
    @Schema(title = "id")
    @NotNull(message = "id不能为空")
    private Integer id;

    // 可以添加更新特有的字段
}
```

**步骤 2：定义 VO**
```java
@Data
@Schema(name = "应用账号信息 详情VO")
public class ApplicationAccountDetailVo implements DetailVo {
    @Schema(title = "id")
    private Integer id;

    @Schema(title = "应用名称")
    private String name;

    @Schema(title = "账号")
    private String account;

    // 其他字段...
}
```

**步骤 3：定义 Service 接口**
```java
public interface BaseApplicationAccountService extends WebService<
    ApplicationAccountAddDto,      // AddDto 泛型
    ApplicationAccountUpdateDto,   // UpdateDto 泛型
    ApplicationAccountDetailVo,    // DetailVo 泛型
    Integer                        // ID 类型泛型
> {
    // 可以添加自定义业务方法
    PageVo<ApplicationAccountPageVo> page(ApplicationAccountPageDto dto);
    String viewPassword(Integer id);
}
```

**步骤 4：实现 Service**
```java
@Service
@Slf4j
public class BaseApplicationAccountServiceImpl extends WebServiceImpl<
    BaseApplicationAccountDao,           // Dao 泛型
    BaseApplicationAccountMapper,        // Mapper 泛型
    BaseApplicationAccountEntity,        // Entity 泛型
    ApplicationAccountAddDto,            // AddDto 泛型
    ApplicationAccountUpdateDto,         // UpdateDto 泛型
    ApplicationAccountDetailVo,          // DetailVo 泛型
    Integer                              // ID 类型泛型
> implements BaseApplicationAccountService {

    @Autowired
    public BaseApplicationAccountServiceImpl(BaseApplicationAccountDao baseDao) {
        super(baseDao);
    }

    // 可以重写父类方法添加自定义逻辑
    @Override
    @Transactional
    public Integer add(ApplicationAccountAddDto dto) {
        // 自定义逻辑（如加密）
        dto.setPassword(encrypt(dto.getPassword()));
        return super.add(dto);
    }

    // 实现自定义业务方法
    @Override
    public PageVo<ApplicationAccountPageVo> page(ApplicationAccountPageDto dto) {
        LambdaQueryWrapper<BaseApplicationAccountEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dto.getType() != null, BaseApplicationAccountEntity::getType, dto.getType())
                .like(dto.getName() != null, BaseApplicationAccountEntity::getName, dto.getName());
        queryWrapper.orderByDesc(BaseApplicationAccountEntity::getId);
        return getBaseDao().page(dto, queryWrapper, ApplicationAccountPageVo.class);
    }
}
```

**步骤 5：实现 Controller**
```java
@RestController
@RequestMapping("/application/account")
@Validated
@Tag(name = "应用账号信息接口")
public class BaseApplicationAccountController extends WebController<
    BaseApplicationAccountService,      // Service 泛型
    ApplicationAccountAddDto,            // AddDto 泛型
    ApplicationAccountUpdateDto,         // UpdateDto 泛型
    ApplicationAccountDetailVo,          // DetailVo 泛型
    Integer                              // ID 类型泛型
> {

    @Autowired
    public BaseApplicationAccountController(BaseApplicationAccountService webService) {
        super(webService);
    }

    // 自动继承以下接口：
    // POST /application/account/add
    // PUT /application/account/update
    // DELETE /application/account/delete
    // GET /application/account/detail

    // 可以添加自定义接口
    @PostMapping("/page")
    @Operation(summary = "分页查询应用账号信息")
    public PageVo<ApplicationAccountPageVo> page(@RequestBody @Valid ApplicationAccountPageDto dto) {
        return getWebService().page(dto);
    }
}
```

**优势：**
- 减少重复代码，提高开发效率
- 统一 CRUD 接口规范
- 自动处理 DTO/Entity/VO 转换
- 自动添加事务支持
- 易于扩展自定义业务逻辑

**注意事项：**
- UpdateDto 通常继承 AddDto，避免字段重复定义
- 可以重写父类方法添加自定义逻辑（如加密、校验）
- 复杂业务逻辑建议添加自定义方法，不要强行使用通用方法
- 通过 `getWebService()` 或 `getBaseDao()` 访问底层服务

### 注释规范

```java
/**
 * 用户服务接口
 *
 * @author itwray
 * @since 2024-01-01
 */
public interface UserService {

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserVo detail(Integer id);
}
```

## Common Tasks

### 创建新的微服务

1. **在 `iw-packaging-parent` 下创建新模块**
   ```bash
   cd iw-packaging-parent
   mkdir iw-new-service
   ```

2. **创建 `pom.xml`**
   ```xml
   <parent>
       <artifactId>iw-packaging-parent</artifactId>
       <groupId>com.itwray.mixes</groupId>
       <version>${revision}</version>
   </parent>
   <artifactId>iw-new-service</artifactId>

   <dependencies>
       <dependency>
           <groupId>com.itwray.mixes</groupId>
           <artifactId>iw-web</artifactId>
       </dependency>
   </dependencies>
   ```

3. **创建启动类**
   ```java
   @SpringBootApplication
   public class IwNewServiceApplication {
       public static void main(String[] args) {
           SpringApplication.run(IwNewServiceApplication.class, args);
       }
   }
   ```

4. **创建配置文件** `src/main/resources/application.yml`

5. **在父 POM 中添加模块**
   ```xml
   <modules>
       <module>iw-new-service</module>
   </modules>
   ```

### 使用代码生成器

1. **配置数据库连接**
   编辑 `iw-code-generator` 中的配置文件

2. **运行生成器**
   ```bash
   cd iw-code-generator
   mvn spring-boot:run
   ```

3. **输入参数**
   - 表名：`auth_user`
   - 模块名：`auth`
   - 包路径：`com.itwray.iw.auth`

4. **生成的文件**
   - Entity: `AuthUser.java`
   - Mapper: `AuthUserMapper.java` + `AuthUserMapper.xml`
   - Service: `AuthUserService.java` + `AuthUserServiceImpl.java`
   - Controller: `AuthUserController.java`

### 添加 Feign 客户端

1. **在 `iw-feign-client` 下创建客户端模块**
   ```bash
   cd iw-feign-client
   mkdir iw-new-service-client
   ```

2. **定义 Feign 接口**
   ```java
   @FeignClient(name = "iw-new-service")
   public interface NewServiceClient {

       @GetMapping("/new-service/api/resource")
       GeneralResponse<ResourceVo> getResource(@RequestParam Integer id);
   }
   ```

3. **在服务中使用**
   ```java
   @Autowired
   private NewServiceClient newServiceClient;

   public void doSomething() {
       GeneralResponse<ResourceVo> response = newServiceClient.getResource(1);
       ResourceVo data = response.getData();
   }
   ```

### 实现分页查询

```java
// Controller
@GetMapping("/page")
public IPage<UserVo> page(UserQueryDto dto) {
    return userService.page(dto);
}

// Service
public IPage<UserVo> page(UserQueryDto dto) {
    Page<AuthUser> page = new Page<>(dto.getPageNum(), dto.getPageSize());
    LambdaQueryWrapper<AuthUser> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(StringUtils.isNotBlank(dto.getUsername()),
                 AuthUser::getUsername, dto.getUsername());

    IPage<AuthUser> result = authUserMapper.selectPage(page, wrapper);
    return result.convert(this::convertToVo);
}
```

### 添加定时任务

```java
@Component
@EnableScheduling
public class DataSyncJob {

    /**
     * 每天凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void syncData() {
        // 定时任务逻辑
    }
}
```

### 使用 Redis 缓存

```java
@Autowired
private RedisTemplate<String, Object> redisTemplate;

// 设置缓存（带过期时间）
redisTemplate.opsForValue().set("auth:user:token:" + userId, token, 7, TimeUnit.DAYS);

// 获取缓存
String token = (String) redisTemplate.opsForValue().get("auth:user:token:" + userId);

// 删除缓存
redisTemplate.delete("auth:user:token:" + userId);
```

## Development Workflow

### 本地开发环境搭建

1. **安装依赖服务**
   - JDK 17
   - Maven 3.8+
   - MySQL 8.x
   - Redis 7.x
   - Nacos 2.3.2
   - RocketMQ 5.3.0（可选）

2. **初始化数据库**
   ```bash
   mysql -u root -p < scripts/database-init.sql
   ```

3. **启动 Nacos**
   ```bash
   # 单机模式
   sh nacos/bin/startup.sh -m standalone
   ```

4. **启动 Redis**
   ```bash
   redis-server
   ```

5. **启动微服务**
   ```bash
   # 启动网关
   cd iw-packaging-parent/iw-gateway
   mvn spring-boot:run

   # 启动认证服务
   cd iw-packaging-parent/iw-auth
   mvn spring-boot:run

   # 启动其他服务...
   ```

### API 文档访问

每个服务都集成了 SpringDoc（OpenAPI 3.0）：

- **iw-auth**: http://localhost:18001/doc.html
- **iw-bookkeeping**: http://localhost:18004/doc.html
- **iw-gateway**: http://localhost:18000/doc.html

### 调试技巧

**查看服务注册情况：**
- Nacos 控制台：http://localhost:8848/nacos
- 默认账号密码：nacos/nacos

**查看日志：**
```bash
# 服务日志位置
logs/{service-name}/
```

**远程调试：**
```bash
# 启动时添加 JVM 参数
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar app.jar
```

## Database Management

### 数据库初始化

```bash
# 创建数据库和用户
mysql -u root -p < scripts/database-init.sql

# 各服务的表结构脚本位于各自的 scripts 目录
cd iw-packaging-parent/iw-auth/scripts
mysql -u iw_root -p iw_mixes < auth_tables.sql
```

### 数据库连接配置

在 Nacos 配置中心配置（或本地 application-dev.yml）：

```yaml
iw:
  db:
    driver-class: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/iw_mixes?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: iw_root
    password: password
```

## Troubleshooting

### 服务无法注册到 Nacos

**检查项：**
1. Nacos 服务是否启动：`curl http://localhost:8848/nacos`
2. 配置的 Nacos 地址是否正确
3. 网络是否可达
4. 查看服务日志中的错误信息

### Feign 调用失败

**常见原因：**
1. 目标服务未启动或未注册到 Nacos
2. 服务名称配置错误
3. 接口路径不匹配
4. 参数序列化问题

**解决方法：**
```java
// 开启 Feign 日志
logging:
  level:
    com.itwray.iw.*.client: DEBUG
```

### MyBatis-Plus 分页不生效

**检查项：**
1. 是否配置了分页插件
2. 是否使用了 `Page` 对象
3. 数据库方言是否正确

### Redis 连接失败

**检查项：**
1. Redis 服务是否启动：`redis-cli ping`
2. 连接配置是否正确
3. 防火墙是否开放端口

### 构建失败

**常见问题：**
```bash
# 清理并重新构建
mvn clean install -DskipTests -U

# 如果依赖下载失败，配置国内镜像
# 编辑 ~/.m2/settings.xml
<mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

## Testing

### 单元测试

```java
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testGetDetail() {
        UserVo user = userService.detail(1);
        assertNotNull(user);
    }
}
```

### 集成测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/auth-service/user/detail")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
```

## Deployment

### 打包部署

```bash
# 1. 构建项目
mvn clean package -DskipTests

# 2. 上传 JAR 包到服务器
scp iw-packaging-parent/iw-auth/target/iw-auth-0.2.1.jar user@server:/app/

# 3. 启动服务
nohup java -jar iw-auth-0.2.1.jar --spring.profiles.active=prod > logs/app.log 2>&1 &
```

### Docker 部署

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 18001
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
# 构建镜像
docker build -t iw-auth:0.2.1 .

# 运行容器
docker run -d -p 18001:18001 \
  -e SPRING_CLOUD_NACOS_SERVER_ADDR=nacos:8848 \
  --name iw-auth iw-auth:0.2.1
```

## Notes

- 非隐私数据尽量写在 YAML 配置文件中，而非配置中心
- 所有 Redis key 必须设置过期时间
- ID 类型能用 int 就不用 long
- 数据库脚本存放在各服务的 `scripts` 目录
- Feign Client 包路径与对应服务保持一致
- 项目版本号统一在根 POM 的 `<revision>` 属性中管理
