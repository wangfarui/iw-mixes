# 项目介绍

**iw-mixes** 是一个服务端混合项目，旨在用一个项目支持 IW 系统的所有服务端功能，围绕ToC用户提供登录、记账、食谱、任务、积分、信息管理等功能服务的应用。<br/>
<span style="color:#888888;font-size:14px">iw-mixes *所有项目模块的依赖只会来自于项目本身和开源依赖。*</span>

# 项目模块

<table>
<tr><td>ProjectName</td><td>SubModule</td><td>Port</td><td>Description</td></tr>
<tr>
    <td>iw-common</td>
    <td></td>
    <td></td>
    <td>IW项目公共基础模块</td>
</tr>
<tr>
    <td>iw-web</td>
    <td></td>
    <td></td>
    <td>Web服务基础模块</td>
</tr>

<tr>
    <td rowspan="7">iw-packaging-parent</td>
    <td>iw-gateway</td>
    <td>18000</td>
    <td>Gateway网关服务</td>
</tr>
<tr>
    <td>iw-auth</td>
    <td>18001</td>
    <td>用户授权服务、基础服务</td>
</tr>
<tr>
    <td>iw-note</td>
    <td>18002</td>
    <td>笔记服务（受资源影响，暂不开发）</td>
</tr>
<tr>
    <td>iw-eat</td>
    <td>18003</td>
    <td>餐饮服务（受资源影响，暂时共用至iw-bookkeeping服务）</td>
</tr>
<tr>
    <td>iw-bookkeeping</td>
    <td>18004</td>
    <td>记账服务</td>
</tr>
<tr>
    <td>iw-points</td>
    <td>18005</td>
    <td>积分服务（受资源影响，暂时共用至iw-bookkeeping服务）</td>
</tr>
<tr>
    <td>iw-external</td>
    <td>18006</td>
    <td>外部服务（需要外网支持）</td>
</tr>

<tr>
    <td rowspan="2">iw-starter</td>
    <td>iw-redis-starter</td>
    <td></td>
    <td>Redis Starter</td>
</tr>
<tr>
    <td>iw-rocketmq-starter</td>
    <td></td>
    <td>RocketMQ Starter</td>
</tr>

<tr>
    <td>iw-feign-client</td>
    <td></td>
    <td></td>
    <td>Feign Client模块</td>
</tr>

<tr>
    <td>iw-code-generator</td>
    <td></td>
    <td></td>
    <td>代码生成模块</td>
</tr>

<tr>
    <td>iw-oauth2-authorization-server</td>
    <td></td>
    <td>18101</td>
    <td>OAuth2授权服务（暂不开放）</td>
</tr>
</table>

# 快速上手
参考：[QUICKLY_CODING.md](https://github.com/wangfarui/iw-mixes/blob/dev/QUICKLY_CODING.md)
