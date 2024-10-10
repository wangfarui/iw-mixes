# 项目介绍
`iw-mixes`是一个服务端混合项目，旨在用一个项目支持IW系统的所有服务端功能。<br/>
`iw-mixes`所有服务的依赖只会来自于项目本身和开源依赖。

# 项目模块
<table>
<tr><td>ProjectName</td><td>Version</td><td>Port</td><td>Description</td></tr>
<tr>
    <td>iw-common</td>
    <td>0.0.1-SNAPSHOT</td>
    <td></td>
    <td>IW项目公共基础模块</td>
</tr>
<tr>
    <td>iw-web</td>
    <td>0.0.1-SNAPSHOT</td>
    <td></td>
    <td>Web项目基础模块</td>
</tr>
<tr>
    <td>iw-redis-starter</td>
    <td>0.0.1-SNAPSHOT</td>
    <td></td>
    <td>Redis Starter</td>
</tr>
<tr>
    <td>iw-gateway</td>
    <td>0.0.1-SNAPSHOT</td>
    <td>18000</td>
    <td>Gateway网关服务</td>
</tr>
<tr>
    <td>iw-auth</td>
    <td>0.0.1-SNAPSHOT</td>
    <td>18001</td>
    <td>用户授权服务、基础服务</td>
</tr>
<tr>
    <td>iw-note</td>
    <td>0.0.1-SNAPSHOT</td>
    <td>18002</td>
    <td>笔记服务（受资源影响，暂不开发）</td>
</tr>
<tr>
    <td>iw-eat</td>
    <td>0.0.1-SNAPSHOT</td>
    <td>18003</td>
    <td>餐饮服务</td>
</tr>
<tr>
    <td>iw-bookkeeping</td>
    <td>0.0.1-SNAPSHOT</td>
    <td>18004</td>
    <td>记账服务</td>
</tr>
<tr>
    <td>iw-points</td>
    <td>0.0.1-SNAPSHOT</td>
    <td>18005</td>
    <td>积分服务</td>
</tr>
<tr>
    <td>iw-oauth2-authorization-server</td>
    <td>0.0.1-SNAPSHOT</td>
    <td>18101</td>
    <td>OAuth2授权服务（暂不开放）</td>
</tr>
</table>

# 项目规则
规则就是一种约定俗成的模式，并不是一种强制性要求，但希望开发者能够有"肌肉记忆"般的默契，使得开发过程更加得心应手。
* yaml配置：非隐私数据尽量写在yaml中，而非配置中心。
* id长度：能用int，就不用long。
* CRUD习惯：新增使用`add`，编辑使用`update`，删除使用`delete`，详情使用`detail`，分页使用`page`。
* 字典：
  * 无论是一对一、一对多、多对多的数据字典项，都采用\<id:name\>的格式存储和查询。
  * 当数据字典具有业务判断逻辑时，通过前后端枚举+表code的形式配置。
* 数据库脚本：在每个项目下的scripts目录下存放自己项目需要的sql脚本。
* 包路径规则：
  * 所有模块的包路径都应该以`com.itwray.iw`开头。
  * feign-client模块的包路径与对应的web服务模块的包路径保持一致，并保证相同路径下类名不重复。