# AGENTS.md - iw-mixes 后端项目指南

## 项目定位

`iw-mixes` 是 IW 系统的服务端联合工程，围绕 ToC 用户提供登录认证、基础资料、家庭组共享、记账、餐食、任务积分、外部能力等后端服务。当前工程是 Java 17 + Spring Boot 3.2.x + Spring Cloud / Spring Cloud Alibaba 的 Maven 多模块项目，前端入口主要来自：

- `../iw-mixes-app`：uni-app / 微信小程序端。
- `../iw-mixes-web-platform`：Vue Web 管理平台。

开发需求进入本项目时，优先判断它属于哪个业务服务，再沿 Controller -> Service -> Mapper/DAO -> DTO/VO/Entity 的路径修改。

## 技术栈

- Java 17。
- Maven 多模块，根 `pom.xml` 统一管理版本和依赖。
- Spring Boot `3.2.12`。
- Spring Cloud `4.1.5`，Spring Cloud Alibaba `2023.0.3.3`。
- Spring Cloud Gateway、OpenFeign、Nacos。
- MyBatis-Plus，必要时使用 XML Mapper。
- Lombok、Hutool、springdoc-openapi、FastExcel。
- Redis Starter、RocketMQ Starter 是项目内 starter 模块。

## 模块地图

- `iw-common`：通用响应、异常码、基础常量和通用工具。典型响应对象是 `GeneralResponse<T>`。
- `iw-web`：Web 服务基础层，包含统一响应包装、异常处理、认证辅助、共享查询作用域、通用 CRUD 基类、Swagger 配置等。
- `iw-feign-client`：跨服务调用客户端，按业务拆为 `iw-auth-client`、`iw-external-client`、`iw-points-client` 等。
- `iw-starter`：项目内 starter，当前有 `iw-redis-starter`、`iw-rocketmq-starter`。
- `iw-packaging-parent`：可启动业务服务集合。
- `iw-code-generator`：代码生成辅助模块。
- `iw-oauth2-authorization-server`：OAuth2 授权服务，当前 README 标注为暂不开放。

业务服务位于 `iw-packaging-parent`：

- `iw-gateway`：网关服务，端口 `18000`，前端和小程序通常通过它访问后端。
- `iw-auth`：认证、用户、家庭组、字典、文件、网站导航、应用账号、基础 AI 任务。
- `iw-bookkeeping`：记账记录、记账动作、收支统计、预算、钱包、会员订阅、语音记账等。
- `iw-eat`：餐食、菜品、冰箱、菜谱相关接口。当前网关把 `/eat-service/**` 转发到 `iw-bookkeeping-service`，说明部署上可能与记账服务共用。
- `iw-points`：任务、积分记录、积分统计。当前网关把 `/points-service/**` 转发到 `iw-bookkeeping-service`，说明部署上可能与记账服务共用。
- `iw-external`：短信、邮件、AI、ASR、天气、热榜、钉钉、汇率等外部能力。
- `iw-note`：笔记服务，README 标注为暂不开发。

## 网关与接口路径

网关配置在 `iw-packaging-parent/iw-gateway/src/main/resources/application.yml`：

- `/auth-service/**` -> `iw-auth-service`，`StripPrefix=1`。
- `/bookkeeping-service/**` -> `iw-bookkeeping-service`，`StripPrefix=1`。
- `/eat-service/**` -> `iw-bookkeeping-service`，`StripPrefix=1`。
- `/points-service/**` -> `iw-bookkeeping-service`，`StripPrefix=1`。
- `/external-service/api/**` -> `iw-external-service`，保留前缀。
- `/external-service/internal/**` -> `iw-external-service`，带 IP 和密钥过滤。
- `/external-service/wb/**` -> `iw-external-service`，WebSocket。

因此 Controller 上的 `@RequestMapping("/bookkeeping/records")` 对前端通常表现为 `/bookkeeping-service/bookkeeping/records/...`。新增或调整接口时，要同时确认网关前缀、Controller 路径和前端调用路径。

## 代码分层习惯

业务模块通常采用以下结构：

- `controller`：REST 入口，声明 `@RequestMapping`、Swagger 注解、校验注解和共享查询注解。
- `service` / `service.impl`：业务接口和实现，复杂业务规则优先落在这里。
- `mapper`：MyBatis-Plus Mapper。
- `dao`：封装更贴近数据访问的查询或组合操作。
- `model/entity`：数据库实体。
- `model/dto`：入参对象，常配合 `@Valid`。
- `model/vo`：出参对象。
- `model/bo`：业务中间对象。
- `model/enums`：业务枚举。
- `resources/mapper`：需要手写 SQL 时放 XML Mapper。
- `job`、`utils`、`core`、`excel`：按业务需要放定时任务、工具、扩展能力、导入导出。

公共 CRUD 可继承 `iw-web` 的 `WebController` 和 `WebService` 体系，默认提供：

- `POST /add`
- `PUT /update`
- `DELETE /delete?id=`
- `GET /detail?id=`

已有模块中，复杂列表、统计、导入、状态变更等接口通常在具体 Controller 中单独声明。

## 响应、异常与认证

- Controller 方法通常直接返回业务对象、列表、分页对象或 `void`，由 `iw-web` 的 `GeneralResponseWrapperAdvice` 统一包装为 `GeneralResponse`。
- 不要在普通 Controller 中重复手动包一层响应，除非已有代码或注解明确要求。
- 业务异常优先使用项目现有异常类型，让全局异常处理统一转换响应。
- 前端会按 `code == 200` 判断成功，`code == 401` 判断登录失效。
- 请求令牌 header 为 `iwtoken`。
- 涉及家庭组共享数据查询的接口，要关注 `@SharedQueryScope` 及其 AOP 逻辑。

## 新增后端接口流程

1. 定位业务服务：认证/基础资料进 `iw-auth`，记账进 `iw-bookkeeping`，餐食进 `iw-eat`，积分任务进 `iw-points`，外部能力进 `iw-external`。
2. 先看同业务下已有 Controller、Service、Mapper、DTO、VO 的写法，沿用命名和返回风格。
3. 如是 CRUD 资源，优先复用 `WebController` / `WebService` 体系；如是统计、状态流转、导入导出、组合查询，则新增专用方法。
4. DTO 做入参校验，VO 做前端所需出参，不直接把内部中间对象暴露给前端。
5. 涉及数据库新增字段时，同步 Entity、DTO/VO、Mapper XML、统计 SQL、导入导出字段。
6. 跨服务调用时，优先在 `iw-feign-client` 或 `iw-web/client` 中补充 Feign Client，而不是硬编码 HTTP。
7. 前端可见接口需确认网关前缀：Controller 路径前要加 `/auth-service`、`/bookkeeping-service`、`/eat-service` 或 `/points-service`。
8. 修改完成后至少做相关模块编译或测试，跨端需求还要同步检查两个前端调用。

## 数据访问规则

- 简单 CRUD 优先 MyBatis-Plus。
- 复杂列表、统计、联表、年度/月度报表优先查看同模块 XML Mapper 的现有 SQL 风格。
- Mapper 接口和 XML 文件名称保持一致，XML 放在对应服务的 `src/main/resources/mapper`。
- 分页出参常见于 `iw-web` 的 `PageVo`。
- 事务边界放在 Service 层。
- 涉及用户、家庭组、共享范围的数据，要先查同业务中现有 `SharedQueryScope` 和家庭组策略用法。

## 配置与环境

- 服务启动配置通常在各服务的 `src/main/resources/application.yml` 和 `application-dev.yml`。
- Nacos 配置通过 `spring.config.import` 引入，默认服务地址是 `localhost:8848`。
- 网关默认端口 `18000`，各服务端口参考根 README。
- 修改配置时区分本地默认值、Nacos 配置、生产敏感配置，不要提交真实密钥。

## 常用命令

在 `iw-mixes` 目录执行：

```bash
mvn clean compile
mvn -pl iw-packaging-parent/iw-bookkeeping -am test
mvn -pl iw-packaging-parent/iw-gateway -am spring-boot:run
```

如果本机 Maven 依赖或 settings 有定制，优先沿用用户已有命令或项目脚本，不要随意改 Maven 配置。

## AI 开发约定

- 先读本文件，再读根目录 `../AGENTS.md` 了解跨项目关系。
- 不要修改 `target/`、`*.iml`、IDE 缓存、构建产物和本地敏感配置。
- 不要因为根目录显示嵌套仓库有变更而重置项目；三个子项目各自是独立 Git 仓库。
- 需求包含页面或小程序交互时，必须同时检查 `../iw-mixes-app` 或 `../iw-mixes-web-platform` 的 API 调用。
- 修改公共模块 `iw-common`、`iw-web`、`iw-feign-client` 时，要评估所有业务服务影响。
- 新增接口时，文档或最终说明中给出前端可调用的完整网关路径。
- 遇到已有命名不统一时，局部沿用所在模块风格，不做无关重命名。

