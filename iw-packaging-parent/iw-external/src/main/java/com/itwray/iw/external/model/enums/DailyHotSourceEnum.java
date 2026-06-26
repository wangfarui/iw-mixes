package com.itwray.iw.external.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * 每日热点来源。
 *
 * @author wray
 * @since 2026/6/26
 */
@Getter
public enum DailyHotSourceEnum {

    KR36("36kr", "36氪", "人气榜", null, "https://m.36kr.com/hot-list-m"),
    CTO51("51cto", "51CTO", "推荐榜", null, "https://www.51cto.com/"),
    POJIE52("52pojie", "吾爱破解", "最新回复", null, "https://www.52pojie.cn/"),
    ACFUN("acfun", "AcFun", "排行榜 · 全站", "AcFun是一家弹幕视频网站，致力于为每一个人带来欢乐。", "https://www.acfun.cn/rank/list/"),
    BAIDU("baidu", "百度", "热搜榜", null, "https://top.baidu.com/board"),
    BILIBILI("bilibili", "哔哩哔哩", "热榜 · 全站", "你所热爱的，就是你的生活", "https://www.bilibili.com/v/popular/rank/all"),
    COOLAPK("coolapk", "酷安", "热榜", null, "https://www.coolapk.com/"),
    CSDN("csdn", "CSDN", "排行榜", "专业开发者社区", "https://www.csdn.net/"),
    DGTLE("dgtle", "数字尾巴", "热门文章", "分享美好数字生活", "https://www.dgtle.com/"),
    DOUBAN_GROUP("douban-group", "豆瓣讨论", "讨论精选", null, "https://www.douban.com/group/explore"),
    DOUBAN_MOVIE("douban-movie", "豆瓣电影", "新片榜", null, "https://movie.douban.com/chart"),
    DOUYIN("douyin", "抖音", "热榜", "实时上升热点", "https://www.douyin.com"),
    EARTHQUAKE("earthquake", "中国地震台", "地震速报", null, "https://news.ceic.ac.cn/"),
    GAMERES("gameres", "GameRes 游资网", "最新资讯", "游戏行业资讯和资源", "https://www.gameres.com"),
    GEEKPARK("geekpark", "极客公园", "热门文章", "极客公园聚焦互联网领域，跟踪新鲜的科技新闻动态，关注极具创新精神的科技产品。", "https://www.geekpark.net/"),
    GENSHIN("genshin", "原神", "最新动态", null, "https://www.miyoushe.com/ys/home/28"),
    GITHUB("github", "github 趋势", "今日", null, "https://github.com/trending?since=daily"),
    GUOKR("guokr", "果壳", "热门文章", "科技有意思", "https://www.guokr.com/"),
    HACKERNEWS("hackernews", "Hacker News", "Popular", "News about hacking and startups", "https://news.ycombinator.com/"),
    HELLOGITHUB("hellogithub", "HelloGitHub", "热门仓库", "分享 GitHub 上有趣、入门级的开源项目", "https://hellogithub.com/"),
    HISTORY("history", "历史上的今天", "月-日", null, "https://baike.baidu.com/calendar"),
    HONKAI("honkai", "崩坏3", "最新动态", null, "https://www.miyoushe.com/bh3/home/6"),
    HOSTLOC("hostloc", "全球主机交流", "最新回复", null, "https://hostloc.com/"),
    HUPU("hupu", "虎扑", "步行街热帖", null, "https://bbs.hupu.com/all-gambia"),
    HUXIU("huxiu", "虎嗅", "24小时", null, "https://www.huxiu.com/moment/"),
    IFANR("ifanr", "爱范儿", "快讯", "15秒了解全球新鲜事", "https://www.ifanr.com/digest/"),
    ITHOME_XIJIAYI("ithome-xijiayi", "IT之家「喜加一」", "最新动态", "最新最全的「喜加一」游戏动态尽在这里！", "https://www.ithome.com/zt/xijiayi"),
    ITHOME("ithome", "IT之家", "热榜", "爱科技，爱这里 - 前沿科技新闻网站", "https://m.ithome.com/rankm/"),
    JIANSHU("jianshu", "简书", "热门推荐", "一个优质的创作社区", "https://www.jianshu.com/"),
    JUEJIN("juejin", "稀土掘金", "文章榜", null, "https://juejin.cn/hot/articles"),
    KUAISHOU("kuaishou", "快手", "热榜", "快手，拥抱每一种生活", "https://www.kuaishou.com/"),
    LINUXDO("linuxdo", "Linux.do", "热门文章", "Linux 技术社区热搜", "https://linux.do/top/weekly"),
    LOL("lol", "英雄联盟", "更新公告", null, "https://lol.qq.com/gicp/news/423/2/1334/1.html"),
    MIYOUSHE("miyoushe", "米游社 · 崩坏3", "最新公告", null, "https://www.miyoushe.com/"),
    NETEASE_NEWS("netease-news", "网易新闻", "热点榜", null, "https://m.163.com/hot"),
    NEWSMTH("newsmth", "水木社区", "热门话题", "水木社区是一个源于清华的高知社群。", "https://www.newsmth.net/"),
    NGABBS("ngabbs", "NGA", "论坛热帖", "精英玩家俱乐部", "https://ngabbs.com/"),
    NODESEEK("nodeseek", "NodeSeek", "最新", null, "https://www.nodeseek.com/"),
    NYTIMES("nytimes", "纽约时报", "World", null, "https://www.nytimes.com/"),
    PRODUCTHUNT("producthunt", "Product Hunt", "Today", "The best new products, every day", "https://www.producthunt.com/"),
    QQ_NEWS("qq-news", "腾讯新闻", "热点榜", null, "https://news.qq.com/"),
    SINA_NEWS("sina-news", "新浪新闻", "总排行", null, "https://sinanews.sina.cn/"),
    SINA("sina", "新浪网", "新浪热榜", "热榜太多，一个就够", "https://sinanews.sina.cn/"),
    SMZDM("smzdm", "什么值得买", "好文原创", "科学消费，认真生活", "https://www.smzdm.com/top/"),
    SSPAI("sspai", "少数派", "热榜", null, "https://sspai.com/"),
    STARRAIL("starrail", "崩坏：星穹铁道", "最新动态", null, "https://www.miyoushe.com/sr/home/53"),
    THEPAPER("thepaper", "澎湃新闻", "热榜", null, "https://www.thepaper.cn/"),
    TIEBA("tieba", "百度贴吧", "热议榜", "全球领先的中文社区", "https://tieba.baidu.com/hottopic/browse/topicList"),
    TOUTIAO("toutiao", "今日头条", "热榜", null, "https://www.toutiao.com/"),
    V2EX("v2ex", "V2EX", "主题榜", null, "https://www.v2ex.com/"),
    WEATHERALARM("weatheralarm", "中央气象台", "全国气象预警", null, "http://nmc.cn/publish/alarm.html"),
    WEIBO("weibo", "微博", "热搜榜", "实时热点，每分钟更新一次", "https://s.weibo.com/top/summary/"),
    WEREAD("weread", "微信读书", "飙升榜", null, "https://weread.qq.com/"),
    YYSTV("yystv", "游研社", "全部文章", "游戏研究社，爱游戏，懂游戏。", "https://www.yystv.cn/docs"),
    ZHIHU_DAILY("zhihu-daily", "知乎日报", "推荐榜", "每天三次，每次七分钟", "https://daily.zhihu.com/"),
    ZHIHU("zhihu", "知乎", "热榜", null, "https://www.zhihu.com/hot"),
    ;

    private final String source;

    private final String title;

    private final String type;

    private final String description;

    private final String link;

    DailyHotSourceEnum(String source, String title, String type, String description, String link) {
        this.source = source;
        this.title = title;
        this.type = type;
        this.description = description;
        this.link = link;
    }

    public static DailyHotSourceEnum of(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        String normalized = source.trim().toLowerCase(Locale.ROOT);
        for (DailyHotSourceEnum sourceEnum : values()) {
            if (sourceEnum.source.equals(normalized)) {
                return sourceEnum;
            }
        }
        return null;
    }
}
