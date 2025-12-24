# 快速上手
## 项目技术栈
* JDK 17
* SpringBoot 3.x
* SpringCloud 4.x
* SpringCloud Alibaba 2023
* Nacos 2.3.2
* MySQL 8.x
* Redis 7.x
* RocketMQ 5.3.0
* MyBatis-Plus 3.5.5
* Hutool 5.8.26
* SpringDoc 2.3.0

> 更多依赖版本，见 [pom.xml](https://github.com/wangfarui/iw-mixes/blob/dev/pom.xml)

## 项目规则

`iw-mixes`项目规则是一种约定俗成的模式，并不是一种强制性要求，但希望开发者能够有"肌肉记忆"般的默契，使得开发过程更加得心应手。

* yaml配置：非隐私数据尽量写在 yaml 中，而非配置中心。
* id长度：能用 int ，就不用 long 。
* CRUD习惯：新增使用`add`，编辑使用`update`，删除使用`delete`，详情使用`detail`，分页使用`page`。
* 字典：
    * 无论是一对一、一对多、多对多的数据字典项，都采用 \<id:name\> 的格式存储和查询。
    * 当数据字典具有业务判断逻辑时，通过前后端枚举 + 表 code 的形式配置。
* 数据库脚本：在每个项目下的 `scripts` 目录下存放自己项目需要的sql脚本。
* 包路径规则：
    * 所有模块的包路径都应该以`com.itwray.iw`开头。
    * `feign-client`模块的包路径与对应的 web 服务模块的包路径保持一致，并保证相同路径下类名不重复。
* Redis缓存规则: 所有key都应该带有有效期，避免因业务变更导致key无意义又不过期的问题。
