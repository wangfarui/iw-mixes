package com.itwray.iw.external.service.dailyhot.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwray.iw.external.core.dailyhot.DailyHotHttpClient;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotHttpResponse;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotItem;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotResult;
import com.itwray.iw.external.model.enums.DailyHotSourceEnum;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DailyHotApi 兼容来源的内置适配器。
 *
 * @author wray
 * @since 2026/6/26
 */
@Component
public class BuiltInDailyHotProvider extends AbstractDailyHotProvider {

    private static final Pattern BAIDU_DATA_PATTERN = Pattern.compile("<!--s-data:(.*?)-->", Pattern.DOTALL);

    private static final Pattern CEIC_DATA_PATTERN = Pattern.compile("const newdata = (\\[.*?]);", Pattern.DOTALL);

    private static final Pattern SINA_DATA_PATTERN = Pattern.compile("var data = (.*);", Pattern.DOTALL);

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    private final DailyHotHttpClient dailyHotHttpClient;

    private final ObjectMapper objectMapper;

    public BuiltInDailyHotProvider(DailyHotHttpClient dailyHotHttpClient, ObjectMapper objectMapper) {
        this.dailyHotHttpClient = dailyHotHttpClient;
        this.objectMapper = objectMapper;
    }

    public boolean supports(DailyHotSourceEnum source) {
        return source != null && source != DailyHotSourceEnum.BILIBILI
                && source != DailyHotSourceEnum.WEIBO
                && source != DailyHotSourceEnum.ZHIHU
                && source != DailyHotSourceEnum.DOUYIN;
    }

    public DailyHotResult fetch(DailyHotSourceEnum source) {
        switch (source) {
            case KR36:
                return fetch36kr(source);
            case CTO51:
                return fetch51cto(source);
            case POJIE52:
                return fetch52pojie(source);
            case ACFUN:
                return fetchAcfun(source);
            case BAIDU:
                return fetchBaidu(source);
            case COOLAPK:
                return fetchCoolapk(source);
            case CSDN:
                return fetchCsdn(source);
            case DGTLE:
                return fetchDgtle(source);
            case DOUBAN_GROUP:
                return fetchDoubanGroup(source);
            case DOUBAN_MOVIE:
                return fetchDoubanMovie(source);
            case EARTHQUAKE:
                return fetchEarthquake(source);
            case GAMERES:
                return fetchGameres(source);
            case GEEKPARK:
                return fetchGeekpark(source);
            case GENSHIN:
                return fetchMiyoushe(source, "2", "ys", 20);
            case GITHUB:
                return fetchGithub(source);
            case GUOKR:
                return fetchGuokr(source);
            case HACKERNEWS:
                return fetchHackerNews(source);
            case HELLOGITHUB:
                return fetchHelloGithub(source);
            case HISTORY:
                return fetchHistory(source);
            case HONKAI:
                return fetchMiyoushe(source, "1", "bh3", 20);
            case HOSTLOC:
                return fetchRss(source, "https://hostloc.com/forum.php?mod=guide&view=new&rss=1", "https://hostloc.com/");
            case HUPU:
                return fetchHupu(source);
            case HUXIU:
                return fetchHuxiu(source);
            case IFANR:
                return fetchIfanr(source);
            case ITHOME_XIJIAYI:
                return fetchIthomeXijiayi(source);
            case ITHOME:
                return fetchIthome(source);
            case JIANSHU:
                return fetchJianshu(source);
            case JUEJIN:
                return fetchJuejin(source);
            case KUAISHOU:
                return fetchKuaishou(source);
            case LINUXDO:
                return fetchRss(source, "https://linux.do/top.rss?period=weekly", "https://linux.do/");
            case LOL:
                return fetchLol(source);
            case MIYOUSHE:
                return fetchMiyoushe(source, "1", "ys", 30);
            case NETEASE_NEWS:
                return fetchNeteaseNews(source);
            case NEWSMTH:
                return fetchNewsmth(source);
            case NGABBS:
                return fetchNga(source);
            case NODESEEK:
                return fetchRss(source, "https://rss.nodeseek.com/", "https://www.nodeseek.com/");
            case NYTIMES:
                return fetchRss(source, "https://rss.nytimes.com/services/xml/rss/nyt/World.xml", "https://www.nytimes.com/");
            case PRODUCTHUNT:
                return fetchProductHunt(source);
            case QQ_NEWS:
                return fetchQqNews(source);
            case SINA_NEWS:
                return fetchSinaNews(source);
            case SINA:
                return fetchSina(source);
            case SMZDM:
                return fetchSmzdm(source);
            case SSPAI:
                return fetchSspai(source);
            case STARRAIL:
                return fetchMiyoushe(source, "6", "sr", 20);
            case THEPAPER:
                return fetchThepaper(source);
            case TIEBA:
                return fetchTieba(source);
            case TOUTIAO:
                return fetchToutiao(source);
            case V2EX:
                return fetchV2ex(source);
            case WEATHERALARM:
                return fetchWeatherAlarm(source);
            case WEREAD:
                return fetchWeread(source);
            case YYSTV:
                return fetchYystv(source);
            case ZHIHU_DAILY:
                return fetchZhihuDaily(source);
            default:
                return DailyHotResult.failure(source.getSource(), "暂不支持的热点来源");
        }
    }

    private DailyHotResult result(DailyHotSourceEnum source, List<DailyHotItem> items) {
        return DailyHotResult.of(source).setData(items).refreshTotal();
    }

    private DailyHotResult fetchJsonArray(DailyHotSourceEnum source, String url, String listPath,
                                          BiFunction<JsonNode, Integer, DailyHotItem> mapper) {
        JsonNode root = dailyHotHttpClient.getJson(url);
        return result(source, mapArray(nodeAt(root, listPath), mapper));
    }

    private DailyHotResult fetchJsonArray(DailyHotSourceEnum source, String url, Map<String, String> headers,
                                          String listPath, BiFunction<JsonNode, Integer, DailyHotItem> mapper) {
        JsonNode root = dailyHotHttpClient.getJson(url, headers);
        return result(source, mapArray(nodeAt(root, listPath), mapper));
    }

    private List<DailyHotItem> mapArray(JsonNode arrayNode, BiFunction<JsonNode, Integer, DailyHotItem> mapper) {
        List<DailyHotItem> result = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            for (int i = 0; i < arrayNode.size(); i++) {
                DailyHotItem item = mapper.apply(arrayNode.get(i), i);
                if (item != null && StringUtils.isNotBlank(item.getTitle())) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    private DailyHotResult fetch36kr(DailyHotSourceEnum source) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("partner_id", "wap");
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("siteId", 1);
        param.put("platformId", 2);
        body.put("param", param);
        body.put("timestamp", System.currentTimeMillis());
        JsonNode root = dailyHotHttpClient.postJson(
                "https://gateway.36kr.com/api/mis/nav/home/nav/rank/hot",
                body,
                headers("Content-Type", "application/json; charset=utf-8")
        );
        return result(source, mapArray(nodeAt(root, "data.hotRankList"), (item, index) -> {
            JsonNode material = item.path("templateMaterial");
            String id = text(item, "itemId");
            return new DailyHotItem()
                    .setId(id)
                    .setTitle(text(material, "widgetTitle"))
                    .setCover(text(material, "widgetImage"))
                    .setAuthor(text(material, "authorName"))
                    .setHot(longValue(material, "statCollect"))
                    .setTimestamp(parseTime(text(item, "publishTime")))
                    .setUrl("https://www.36kr.com/p/" + id)
                    .setMobileUrl("https://m.36kr.com/p/" + id);
        }));
    }

    private DailyHotResult fetch51cto(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://api-media.51cto.com/index/index/recommend",
                headers("Referer", "https://www.51cto.com/"), "data.data.list", (item, index) -> {
                    String id = firstTextAt(item, "id", "article_id");
                    String url = firstTextAt(item, "url", "article_url");
                    return new DailyHotItem()
                            .setId(id == null ? index : id)
                            .setTitle(firstTextAt(item, "title"))
                            .setDesc(firstTextAt(item, "summary", "description"))
                            .setCover(firstTextAt(item, "cover", "image", "img"))
                            .setAuthor(firstTextAt(item, "author", "nickname"))
                            .setHot(longAt(item, "read_num"))
                            .setUrl(StringUtils.defaultIfBlank(url, "https://www.51cto.com/"))
                            .setMobileUrl(StringUtils.defaultIfBlank(url, "https://www.51cto.com/"));
                });
    }

    private DailyHotResult fetch52pojie(DailyHotSourceEnum source) {
        DailyHotHttpResponse response = dailyHotHttpClient.get(
                "https://www.52pojie.cn/forum.php?mod=guide&view=digest&rss=1",
                headers("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 Mobile Safari/537.36")
        );
        String xml = new String(response.getBodyBytes(), Charset.forName("GBK"));
        return result(source, parseRss(xml, "https://www.52pojie.cn/"));
    }

    private DailyHotResult fetchAcfun(DailyHotSourceEnum source) {
        return fetchJsonArray(source,
                "https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&rankLimit=30&rankPeriod=DAY",
                headers("Referer", "https://www.acfun.cn/rank/list/"),
                "rankList", (item, index) -> {
                    String id = text(item, "dougaId");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "contentTitle"))
                            .setCover(text(item, "coverUrl"))
                            .setAuthor(firstTextAt(item, "userName", "owner.name"))
                            .setHot(longValue(item, "viewCount"))
                            .setTimestamp(timestampMillisAt(item, "createTimeMillis"))
                            .setUrl("https://www.acfun.cn/v/ac" + id)
                            .setMobileUrl("https://m.acfun.cn/v/?ac=" + id);
                });
    }

    private DailyHotResult fetchBaidu(DailyHotSourceEnum source) {
        DailyHotHttpResponse response = dailyHotHttpClient.get("https://top.baidu.com/board?tab=realtime", defaultHeaders());
        Matcher matcher = BAIDU_DATA_PATTERN.matcher(response.getBody());
        if (!matcher.find()) {
            return result(source, new ArrayList<>());
        }
        try {
            JsonNode root = objectMapper.readTree(matcher.group(1));
            JsonNode content = nodeAt(root, "data.cards.0.content");
            if (content.isArray() && content.size() > 0 && content.get(0).has("content")) {
                content = content.get(0).path("content");
            }
            JsonNode finalContent = content;
            return result(source, mapArray(finalContent, (item, index) -> {
                String title = firstTextAt(item, "word", "title");
                return new DailyHotItem()
                        .setId(longAt(item, "index") == null ? index + 1 : longAt(item, "index"))
                        .setTitle(title)
                        .setDesc(firstTextAt(item, "desc"))
                        .setCover(firstTextAt(item, "img", "imgInfo.src"))
                        .setAuthor(firstTextAt(item, "show"))
                        .setHot(parseHotText(firstTextAt(item, "hotScore", "hotTag")))
                        .setUrl("https://www.baidu.com/s?wd=" + encode(firstTextAt(item, "query", "word", "title")))
                        .setMobileUrl(firstTextAt(item, "rawUrl", "url"));
            }));
        } catch (Exception e) {
            throw new IllegalStateException("百度热搜解析失败", e);
        }
    }

    private DailyHotResult fetchCoolapk(DailyHotSourceEnum source) {
        return fetchJsonArray(source,
                "https://api.coolapk.com/v6/page/dataList?url=/feed/statList?cacheExpires=300&statType=day&sortField=detailnum&title=今日热门&subTitle=&page=1",
                coolapkHeaders(),
                "data", (item, index) -> new DailyHotItem()
                        .setId(text(item, "id"))
                        .setTitle(text(item, "message"))
                        .setCover(text(item, "tpic"))
                        .setAuthor(text(item, "username"))
                        .setDesc(text(item, "ttitle"))
                        .setUrl(text(item, "shareUrl"))
                        .setMobileUrl(text(item, "shareUrl")));
    }

    private DailyHotResult fetchCsdn(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://blog.csdn.net/phoenix/web/blog/hot-rank?page=0&pageSize=30",
                "data", (item, index) -> new DailyHotItem()
                        .setId(firstTextAt(item, "articleId", "id"))
                        .setTitle(text(item, "articleTitle"))
                        .setAuthor(text(item, "nickName"))
                        .setHot(longAt(item, "hotRankScore"))
                        .setUrl(text(item, "articleDetailUrl"))
                        .setMobileUrl(text(item, "articleDetailUrl")));
    }

    private DailyHotResult fetchDgtle(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://opser.api.dgtle.com/v2/news/index", "items", (item, index) -> {
            String id = text(item, "id");
            return new DailyHotItem()
                    .setId(id)
                    .setTitle(firstTextAt(item, "title", "content"))
                    .setDesc(firstTextAt(item, "summary", "content"))
                    .setCover(firstTextAt(item, "cover", "pic"))
                    .setAuthor(text(item, "author"))
                    .setTimestamp(timestampMillisAt(item, "created_at"))
                    .setUrl("https://www.dgtle.com/news-" + id + "-" + text(item, "type") + ".html")
                    .setMobileUrl("https://m.dgtle.com/news-details/" + id);
        });
    }

    private DailyHotResult fetchDoubanGroup(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://www.douban.com/group/explore", defaultHeaders()).getBody(),
                "https://www.douban.com/group/explore");
        List<DailyHotItem> items = new ArrayList<>();
        for (Element element : document.select(".article .channel-item")) {
            Element link = element.selectFirst("h3 a");
            if (link == null) {
                continue;
            }
            String url = link.absUrl("href");
            String id = firstNumber(url);
            items.add(new DailyHotItem()
                    .setId(id)
                    .setTitle(link.text().trim())
                    .setCover(element.select(".pic-wrap img").attr("src"))
                    .setDesc(element.select(".block p").text().trim())
                    .setHot(0)
                    .setUrl(url)
                    .setMobileUrl("https://m.douban.com/group/topic/" + id + "/"));
        }
        return result(source, items);
    }

    private DailyHotResult fetchDoubanMovie(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://movie.douban.com/chart", defaultHeaders()).getBody(),
                "https://movie.douban.com/chart");
        List<DailyHotItem> items = new ArrayList<>();
        for (Element element : document.select(".article tr.item")) {
            Element link = element.selectFirst("a");
            if (link == null) {
                continue;
            }
            String url = link.absUrl("href");
            String id = firstNumber(url);
            String score = element.select(".rating_nums").text();
            items.add(new DailyHotItem()
                    .setId(id)
                    .setTitle("【" + StringUtils.defaultIfBlank(score, "0.0") + "】" + link.attr("title"))
                    .setCover(element.select("img").attr("src"))
                    .setDesc(element.select("p.pl").text())
                    .setHot(parseHotText(element.select("span.pl").text()))
                    .setUrl(url)
                    .setMobileUrl("https://m.douban.com/movie/subject/" + id + "/"));
        }
        return result(source, items);
    }

    private DailyHotResult fetchEarthquake(DailyHotSourceEnum source) {
        DailyHotHttpResponse response = dailyHotHttpClient.get("https://news.ceic.ac.cn/speedsearch.html", defaultHeaders());
        Matcher matcher = CEIC_DATA_PATTERN.matcher(response.getBody());
        if (!matcher.find()) {
            return result(source, new ArrayList<>());
        }
        try {
            JsonNode array = objectMapper.readTree(matcher.group(1));
            return result(source, mapArray(array, (item, index) -> {
                String id = text(item, "NEW_DID");
                String location = text(item, "LOCATION_C");
                String magnitude = text(item, "M");
                String desc = "发震时刻(UTC+8)：" + text(item, "O_TIME") + "\n"
                        + "参考位置：" + location + "\n"
                        + "震级(M)：" + magnitude + "\n"
                        + "纬度(°)：" + text(item, "EPI_LAT") + "\n"
                        + "经度(°)：" + text(item, "EPI_LON") + "\n"
                        + "深度(千米)：" + text(item, "EPI_DEPTH");
                return new DailyHotItem()
                        .setId(id)
                        .setTitle(location + "发生" + magnitude + "级地震")
                        .setDesc(desc)
                        .setTimestamp(parseTime(text(item, "O_TIME")))
                        .setUrl("https://news.ceic.ac.cn/" + id + ".html")
                        .setMobileUrl("https://news.ceic.ac.cn/" + id + ".html");
            }));
        } catch (Exception e) {
            throw new IllegalStateException("地震速报解析失败", e);
        }
    }

    private DailyHotResult fetchGameres(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://www.gameres.com", defaultHeaders()).getBody(),
                "https://www.gameres.com");
        List<DailyHotItem> items = new ArrayList<>();
        for (Element element : document.select("a[href*=article], .item, .news-item, li")) {
            Element link = element.tagName().equals("a") ? element : element.selectFirst("a[href]");
            if (link == null || StringUtils.isBlank(link.text())) {
                continue;
            }
            String url = link.absUrl("href");
            if (!url.contains("gameres.com")) {
                continue;
            }
            items.add(new DailyHotItem()
                    .setId(firstNumber(url))
                    .setTitle(link.text().trim())
                    .setUrl(url)
                    .setMobileUrl(url));
            if (items.size() >= 30) {
                break;
            }
        }
        return result(source, items);
    }

    private DailyHotResult fetchGeekpark(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://mainssl.geekpark.net/api/v2", "homepage_posts", (item, index) -> {
            JsonNode post = item.has("post") ? item.path("post") : item;
            String id = text(post, "id");
            return new DailyHotItem()
                    .setId(id)
                    .setTitle(text(post, "title"))
                    .setDesc(firstTextAt(post, "summary", "description"))
                    .setCover(firstTextAt(post, "cover", "cover_url"))
                    .setAuthor(firstTextAt(post, "user.nickname", "author.nickname"))
                    .setTimestamp(timestampMillisAt(post, "published_at"))
                    .setUrl("https://www.geekpark.net/news/" + id)
                    .setMobileUrl("https://www.geekpark.net/news/" + id);
        });
    }

    private DailyHotResult fetchGithub(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://github.com/trending?since=daily", githubHeaders()).getBody(),
                "https://github.com");
        List<DailyHotItem> items = new ArrayList<>();
        int index = 0;
        for (Element element : document.select("article.Box-row")) {
            Element link = element.selectFirst("h2 a");
            if (link == null) {
                continue;
            }
            String fullName = link.text().replace("\n", "").replaceAll("\\s+", " ").trim();
            String repo = fullName.contains("/") ? fullName.substring(fullName.indexOf('/') + 1).trim() : fullName;
            String url = link.absUrl("href");
            items.add(new DailyHotItem()
                    .setId(index++)
                    .setTitle(repo)
                    .setDesc(element.select("p.col-9.color-fg-muted").text().trim())
                    .setAuthor(fullName.contains("/") ? fullName.substring(0, fullName.indexOf('/')).trim() : null)
                    .setHot(element.select("a[href$=/stargazers]").text().trim())
                    .setUrl(url)
                    .setMobileUrl(url));
        }
        return result(source, items);
    }

    private DailyHotResult fetchGuokr(DailyHotSourceEnum source) {
        JsonNode root = dailyHotHttpClient.getJson("https://www.guokr.com/beta/proxy/science_api/articles?limit=30",
                headers("Referer", "https://www.guokr.com/"));
        return result(source, mapArray(root, (item, index) -> {
            String id = text(item, "id");
            return new DailyHotItem()
                    .setId(id)
                    .setTitle(text(item, "title"))
                    .setDesc(text(item, "summary"))
                    .setCover(firstTextAt(item, "small_image", "image"))
                    .setAuthor(firstTextAt(item, "author.nickname", "author"))
                    .setTimestamp(timestampMillisAt(item, "date_published"))
                    .setUrl("https://www.guokr.com/article/" + id)
                    .setMobileUrl("https://m.guokr.com/article/" + id);
        }));
    }

    private DailyHotResult fetchHackerNews(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://news.ycombinator.com", defaultHeaders()).getBody(),
                "https://news.ycombinator.com");
        List<DailyHotItem> items = new ArrayList<>();
        for (Element element : document.select(".athing")) {
            String id = element.attr("id");
            Element link = element.selectFirst(".titleline a");
            if (link == null) {
                continue;
            }
            Element score = document.selectFirst("#score_" + id);
            String url = link.absUrl("href");
            items.add(new DailyHotItem()
                    .setId(id)
                    .setTitle(link.text().trim())
                    .setHot(score == null ? null : parseHotText(score.text()))
                    .setUrl(StringUtils.defaultIfBlank(url, "https://news.ycombinator.com/item?id=" + id))
                    .setMobileUrl(StringUtils.defaultIfBlank(url, "https://news.ycombinator.com/item?id=" + id)));
        }
        return result(source, items);
    }

    private DailyHotResult fetchHelloGithub(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://abroad.hellogithub.com/v1/?sort_by=featured&tid=&page=1",
                "data", (item, index) -> {
                    String id = text(item, "item_id");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "title"))
                            .setDesc(text(item, "summary"))
                            .setAuthor(text(item, "author"))
                            .setHot(longValue(item, "clicks_total"))
                            .setTimestamp(parseTime(text(item, "updated_at")))
                            .setUrl("https://hellogithub.com/repository/" + id)
                            .setMobileUrl("https://hellogithub.com/repository/" + id);
                });
    }

    private DailyHotResult fetchHistory(DailyHotSourceEnum source) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        String month = String.format("%02d", now.getMonthValue());
        String day = String.format("%02d", now.getDayOfMonth());
        JsonNode root = dailyHotHttpClient.getJson("https://baike.baidu.com/cms/home/eventsOnHistory/" + month + ".json");
        return result(source, mapArray(nodeAt(root, month + "." + month + day), (item, index) -> new DailyHotItem()
                .setId(index + 1)
                .setTitle(stripHtml(text(item, "title")))
                .setDesc(stripHtml(text(item, "desc")))
                .setUrl(text(item, "link"))
                .setMobileUrl(text(item, "link"))));
    }

    private DailyHotResult fetchHupu(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://m.hupu.com/api/v2/bbs/topicThreads?topicId=1&page=1",
                "data.topicThreads", (item, index) -> {
                    String id = text(item, "tid");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "title"))
                            .setAuthor(text(item, "username"))
                            .setHot(longAt(item, "replies"))
                            .setUrl("https://bbs.hupu.com/" + id + ".html")
                            .setMobileUrl("https://bbs.hupu.com/" + id + ".html");
                });
    }

    private DailyHotResult fetchHuxiu(DailyHotSourceEnum source) {
        return fetchJsonArray(source,
                "https://moment-api.huxiu.com/web-v3/moment/feed?platform=www",
                headers("Referer", "https://www.huxiu.com/moment/"),
                "data.moment_list.datalist", (item, index) -> {
                    String id = text(item, "object_id");
                    String content = StringUtils.defaultString(text(item, "content")).replaceAll("<br\\s*/?>", "\n");
                    String[] lines = content.split("\\n");
                    String title = lines.length == 0 ? "" : stripHtml(lines[0]).replaceAll("。$", "");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(title)
                            .setDesc(stripHtml(content.replaceFirst(Pattern.quote(lines.length == 0 ? "" : lines[0]), "")))
                            .setAuthor(textAt(item, "user_info.username"))
                            .setHot(longAt(item, "count_info.agree_num"))
                            .setTimestamp(parseTime(text(item, "publish_time")))
                            .setUrl("https://www.huxiu.com/moment/" + id + ".html")
                            .setMobileUrl("https://m.huxiu.com/moment/" + id + ".html");
                });
    }

    private DailyHotResult fetchIfanr(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://sso.ifanr.com/api/v5/wp/buzz/?limit=20&offset=0",
                "objects", (item, index) -> {
                    String id = text(item, "post_id");
                    String url = firstTextAt(item, "buzz_original_url");
                    if (StringUtils.isBlank(url)) {
                        url = "https://www.ifanr.com/" + id;
                    }
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "post_title"))
                            .setDesc(stripHtml(text(item, "excerpt")))
                            .setCover(firstTextAt(item, "image", "post_image"))
                            .setTimestamp(timestampMillisAt(item, "created_at"))
                            .setUrl(url)
                            .setMobileUrl(url);
                });
    }

    private DailyHotResult fetchIthome(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://m.ithome.com/rankm/", defaultHeaders()).getBody(),
                "https://m.ithome.com/rankm/");
        List<DailyHotItem> items = new ArrayList<>();
        for (Element element : document.select(".rank-box .placeholder")) {
            Element link = element.selectFirst("a");
            String href = link == null ? "" : link.absUrl("href");
            String id = firstNumber(href);
            String desktop = ithomeDesktopLink(href);
            items.add(new DailyHotItem()
                    .setId(id)
                    .setTitle(element.select(".plc-title").text().trim())
                    .setCover(element.select("img").attr("data-original"))
                    .setHot(parseHotText(element.select(".review-num").text()))
                    .setUrl(desktop)
                    .setMobileUrl(desktop));
        }
        return result(source, items);
    }

    private DailyHotResult fetchIthomeXijiayi(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://www.ithome.com/zt/xijiayi", defaultHeaders()).getBody(),
                "https://www.ithome.com/zt/xijiayi");
        List<DailyHotItem> items = new ArrayList<>();
        for (Element element : document.select(".newslist li")) {
            Element link = element.selectFirst("a");
            String href = link == null ? "" : link.absUrl("href");
            items.add(new DailyHotItem()
                    .setId(firstNumber(href))
                    .setTitle(element.select(".newsbody h2").text().trim())
                    .setDesc(element.select(".newsbody p").text().trim())
                    .setCover(element.select("img").attr("data-original"))
                    .setHot(parseHotText(element.select(".comment").text()))
                    .setUrl(href)
                    .setMobileUrl(ithomeMobileLink(href)));
        }
        return result(source, items);
    }

    private DailyHotResult fetchJianshu(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://www.jianshu.com/", headers("Referer", "https://www.jianshu.com")).getBody(),
                "https://www.jianshu.com/");
        List<DailyHotItem> items = new ArrayList<>();
        for (Element element : document.select("ul.note-list li")) {
            Element link = element.selectFirst("a.title");
            if (link == null) {
                continue;
            }
            String url = link.absUrl("href");
            items.add(new DailyHotItem()
                    .setId(url.replaceFirst(".*/", ""))
                    .setTitle(link.text().trim())
                    .setCover(element.select("img").attr("src"))
                    .setDesc(element.select("p.abstract").text().trim())
                    .setAuthor(element.select("a.nickname").text().trim())
                    .setUrl(url)
                    .setMobileUrl(url));
        }
        return result(source, items);
    }

    private DailyHotResult fetchJuejin(DailyHotSourceEnum source) {
        return fetchJsonArray(source,
                "https://api.juejin.cn/content_api/v1/content/article_rank?category_id=1&type=hot",
                defaultHeaders(),
                "data", (item, index) -> {
                    String id = textAt(item, "content.content_id");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(textAt(item, "content.title"))
                            .setAuthor(textAt(item, "author.name"))
                            .setHot(longAt(item, "content_counter.hot_rank"))
                            .setUrl("https://juejin.cn/post/" + id)
                            .setMobileUrl("https://juejin.cn/post/" + id);
                });
    }

    private DailyHotResult fetchKuaishou(DailyHotSourceEnum source) {
        String html = dailyHotHttpClient.get("https://www.kuaishou.com/?isHome=1", defaultHeaders()).getBody();
        String prefix = "window.__APOLLO_STATE__=";
        int start = html.indexOf(prefix);
        if (start < 0) {
            return result(source, new ArrayList<>());
        }
        String scriptSlice = html.substring(start + prefix.length());
        int sentinelA = scriptSlice.indexOf(";(function(");
        int sentinelB = scriptSlice.indexOf("</script>");
        int cutIndex = sentinelA >= 0 && sentinelB >= 0 ? Math.min(sentinelA, sentinelB) : Math.max(sentinelA, sentinelB);
        if (cutIndex < 0) {
            return result(source, new ArrayList<>());
        }
        String raw = scriptSlice.substring(0, cutIndex).trim().replaceAll(";$", "");
        raw = raw.substring(0, raw.lastIndexOf('}') + 1);
        try {
            JsonNode defaultClient = objectMapper.readTree(raw).path("defaultClient");
            JsonNode items = defaultClient.path("$ROOT_QUERY.visionHotRank({\"page\":\"home\"})").path("items");
            if (!items.isArray()) {
                items = defaultClient.path("$ROOT_QUERY.visionHotRank({\"page\":\"home\",\"platform\":\"web\"})").path("items");
            }
            JsonNode finalItems = items;
            return result(source, mapArray(finalItems, (item, index) -> {
                JsonNode hotItem = defaultClient.path(text(item, "id"));
                String id = textAt(hotItem, "photoIds.json.0");
                return new DailyHotItem()
                        .setId(text(hotItem, "id"))
                        .setTitle(text(hotItem, "name"))
                        .setCover(decodeUrl(text(hotItem, "poster")))
                        .setHot(parseHotText(text(hotItem, "hotValue")))
                        .setUrl("https://www.kuaishou.com/short-video/" + id)
                        .setMobileUrl("https://www.kuaishou.com/short-video/" + id);
            }));
        } catch (Exception e) {
            throw new IllegalStateException("快手热榜解析失败", e);
        }
    }

    private DailyHotResult fetchLol(DailyHotSourceEnum source) {
        return fetchJsonArray(source,
                "https://apps.game.qq.com/cmc/zmMcnTargetContentList?r0=json&page=1&num=30&target=24&source=web_pc",
                "data.result", (item, index) -> {
                    String id = text(item, "iDocID");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "sTitle"))
                            .setCover(text(item, "sIMG"))
                            .setTimestamp(timestampMillisAt(item, "dtCreateTime"))
                            .setUrl("https://lol.qq.com/news/detail.shtml?docid=" + encode(id))
                            .setMobileUrl("https://lol.qq.com/news/detail.shtml?docid=" + encode(id));
                });
    }

    private DailyHotResult fetchMiyoushe(DailyHotSourceEnum source, String gids, String pathPrefix, int pageSize) {
        String url = "https://bbs-api-static.miyoushe.com/painter/wapi/getNewsList?client_type=4&gids="
                + gids + "&last_id=&page_size=" + pageSize + "&type=1";
        return fetchJsonArray(source, url, "data.list", (item, index) -> {
            JsonNode post = item.path("post");
            String id = text(post, "post_id");
            return new DailyHotItem()
                    .setId(id)
                    .setTitle(text(post, "subject"))
                    .setDesc(text(post, "content"))
                    .setCover(firstTextAt(post, "cover", "images.0"))
                    .setAuthor(textAt(item, "user.nickname"))
                    .setHot(longValue(post, "view_status"))
                    .setTimestamp(timestampMillisAt(post, "created_at"))
                    .setUrl("https://www.miyoushe.com/" + pathPrefix + "/article/" + id)
                    .setMobileUrl("https://m.miyoushe.com/" + pathPrefix + "/#/article/" + id);
        });
    }

    private DailyHotResult fetchNeteaseNews(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://m.163.com/fe/api/hot/news/flow",
                "data.list", (item, index) -> {
                    String id = text(item, "docid");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "title"))
                            .setCover(text(item, "imgsrc"))
                            .setHot(parseHotText(firstTextAt(item, "replyCount", "replyCountText")))
                            .setUrl("https://www.163.com/dy/article/" + id + ".html")
                            .setMobileUrl("https://m.163.com/dy/article/" + id + ".html");
                });
    }

    private DailyHotResult fetchNewsmth(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://wap.newsmth.net/wap/api/hot/global",
                "data.topics", (item, index) -> {
                    JsonNode article = item.path("article");
                    String topicId = text(article, "topicId");
                    String url = "https://wap.newsmth.net/article/" + topicId + "?title=" + encode(textAt(item, "board.title")) + "&from=home";
                    return new DailyHotItem()
                            .setId(text(item, "firstArticleId"))
                            .setTitle(text(article, "subject"))
                            .setDesc(text(article, "body"))
                            .setAuthor(textAt(article, "account.name"))
                            .setTimestamp(parseTime(text(article, "postTime")))
                            .setUrl(url)
                            .setMobileUrl(url);
                });
    }

    private DailyHotResult fetchNga(DailyHotSourceEnum source) {
        Map<String, String> headers = headers(
                "Accept", "*/*",
                "Referer", "https://ngabbs.com/",
                "Content-Type", "application/x-www-form-urlencoded",
                "X-User-Agent", "NGA_skull/7.3.1(iPhone13,2;iOS 17.2.1)"
        );
        JsonNode root = dailyHotHttpClient.postJson(
                "https://ngabbs.com/nuke.php?__lib=load_topic&__act=load_topic_reply_ladder2&opt=1&all=1",
                "__output=14",
                headers
        );
        return result(source, mapArray(nodeAt(root, "result.0"), (item, index) -> {
            String url = "https://bbs.nga.cn" + text(item, "tpcurl");
            return new DailyHotItem()
                    .setId(text(item, "tid"))
                    .setTitle(text(item, "subject"))
                    .setAuthor(text(item, "author"))
                    .setHot(longValue(item, "replies"))
                    .setTimestamp(timestampMillisAt(item, "postdate"))
                    .setUrl(url)
                    .setMobileUrl(url);
        }));
    }

    private DailyHotResult fetchProductHunt(DailyHotSourceEnum source) {
        Document document = Jsoup.parse(dailyHotHttpClient.get("https://www.producthunt.com/", defaultHeaders()).getBody(),
                "https://www.producthunt.com/");
        List<DailyHotItem> items = new ArrayList<>();
        for (Element element : document.select("[data-test=homepage-section-0] [data-test^=post-item]")) {
            Element link = element.selectFirst("a");
            String title = element.select("a[data-test^=post-name]").text().trim();
            if (link == null || StringUtils.isBlank(title)) {
                continue;
            }
            String id = element.attr("data-test").replace("post-item-", "");
            items.add(new DailyHotItem()
                    .setId(id)
                    .setTitle(title)
                    .setHot(parseHotText(element.select("[data-test=vote-button]").text()))
                    .setUrl(link.absUrl("href"))
                    .setMobileUrl(link.absUrl("href")));
        }
        return result(source, items);
    }

    private DailyHotResult fetchQqNews(DailyHotSourceEnum source) {
        JsonNode root = dailyHotHttpClient.getJson("https://r.inews.qq.com/gw/event/hot_ranking_list?page_size=50");
        JsonNode list = nodeAt(root, "idlist.0.newslist");
        List<DailyHotItem> items = mapArray(list, (item, index) -> {
            if (index == 0) {
                return null;
            }
            String id = text(item, "id");
            return new DailyHotItem()
                    .setId(id)
                    .setTitle(text(item, "title"))
                    .setCover(text(item, "thumbnails_qqnews.0"))
                    .setHot(longAt(item, "readCount"))
                    .setUrl("https://new.qq.com/rain/a/" + id)
                    .setMobileUrl("https://view.inews.qq.com/k/" + id);
        });
        return result(source, items);
    }

    private DailyHotResult fetchSinaNews(DailyHotSourceEnum source) {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        String date = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        String url = "https://top.news.sina.com.cn/ws/GetTopDataList.php?top_type=day&top_cat=www_www_all_suda_suda&top_time="
                + date + "&top_show_num=50";
        String body = dailyHotHttpClient.get(url, defaultHeaders()).getBody().trim();
        Matcher matcher = SINA_DATA_PATTERN.matcher(body);
        if (!matcher.find()) {
            return result(source, new ArrayList<>());
        }
        try {
            JsonNode list = objectMapper.readTree(matcher.group(1)).path("data");
            return result(source, mapArray(list, (item, index) -> new DailyHotItem()
                    .setId(text(item, "id"))
                    .setTitle(text(item, "title"))
                    .setAuthor(text(item, "media"))
                    .setHot(parseHotText(text(item, "top_num")))
                    .setTimestamp(parseTime(text(item, "create_date") + " " + text(item, "create_time")))
                    .setUrl(text(item, "url"))
                    .setMobileUrl(text(item, "url"))));
        } catch (Exception e) {
            throw new IllegalStateException("新浪新闻解析失败", e);
        }
    }

    private DailyHotResult fetchSina(DailyHotSourceEnum source) {
        return fetchJsonArray(source,
                "https://newsapp.sina.cn/api/hotlist?newsId=HB-1-snhs%2Ftop_news_list-1",
                "data.hotList", (item, index) -> {
                    JsonNode info = item.has("info") ? item.path("info") : item;
                    String url = firstTextAt(info, "url", "base.base.url");
                    return new DailyHotItem()
                            .setId(firstTextAt(info, "id", "docid"))
                            .setTitle(firstTextAt(info, "title", "base.base.title"))
                            .setCover(firstTextAt(info, "img", "base.base.img"))
                            .setHot(longAt(info, "hotValue"))
                            .setUrl(url)
                            .setMobileUrl(url);
                });
    }

    private DailyHotResult fetchSmzdm(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://post.smzdm.com/rank/json_more/?unit=day",
                "data", (item, index) -> {
                    String url = text(item, "jump_link");
                    return new DailyHotItem()
                            .setId(firstTextAt(item, "article_id", "id"))
                            .setTitle(text(item, "title"))
                            .setCover(text(item, "pic_url"))
                            .setAuthor(text(item, "nickname"))
                            .setHot(longAt(item, "fav_num"))
                            .setUrl(url)
                            .setMobileUrl(url);
                });
    }

    private DailyHotResult fetchSspai(DailyHotSourceEnum source) {
        return fetchJsonArray(source,
                "https://sspai.com/api/v1/article/tag/page/get?limit=40&tag=" + encode("热门文章"),
                "data", (item, index) -> {
                    String id = text(item, "id");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "title"))
                            .setDesc(text(item, "summary"))
                            .setCover(text(item, "banner"))
                            .setAuthor(textAt(item, "author.nickname"))
                            .setHot(longValue(item, "like_count"))
                            .setTimestamp(timestampMillisAt(item, "released_time"))
                            .setUrl("https://sspai.com/post/" + id)
                            .setMobileUrl("https://sspai.com/post/" + id);
                });
    }

    private DailyHotResult fetchThepaper(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://cache.thepaper.cn/contentapi/wwwIndex/rightSidebar",
                "data.hotNews", (item, index) -> {
                    String id = text(item, "contId");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "name"))
                            .setHot(longValue(item, "praiseTimes"))
                            .setUrl("https://www.thepaper.cn/newsDetail_forward_" + id)
                            .setMobileUrl("https://m.thepaper.cn/newsDetail_forward_" + id);
                });
    }

    private DailyHotResult fetchTieba(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://tieba.baidu.com/hottopic/browse/topicList",
                "data.bang_topic.topic_list", (item, index) -> new DailyHotItem()
                        .setId(text(item, "topic_id"))
                        .setTitle(text(item, "topic_name"))
                        .setDesc(text(item, "topic_desc"))
                        .setCover(text(item, "topic_pic"))
                        .setHot(longValue(item, "discuss_num"))
                        .setUrl("https://tieba.baidu.com/hottopic/browse/hottopic?topic_id=" + text(item, "topic_id"))
                        .setMobileUrl("https://tieba.baidu.com/hottopic/browse/hottopic?topic_id=" + text(item, "topic_id")));
    }

    private DailyHotResult fetchToutiao(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc",
                "data", (item, index) -> {
                    String id = text(item, "ClusterIdStr");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "Title"))
                            .setCover(textAt(item, "Image.url"))
                            .setHot(longValue(item, "HotValue"))
                            .setUrl("https://www.toutiao.com/trending/" + id + "/")
                            .setMobileUrl("https://api.toutiaoapi.com/feoffline/amos_land/new/html/main/index.html?topic_id=" + id);
                });
    }

    private DailyHotResult fetchV2ex(DailyHotSourceEnum source) {
        JsonNode root = dailyHotHttpClient.getJson("https://www.v2ex.com/api/topics/hot.json");
        return result(source, mapArray(root, (item, index) -> new DailyHotItem()
                .setId(text(item, "id"))
                .setTitle(text(item, "title"))
                .setDesc(text(item, "content"))
                .setAuthor(textAt(item, "member.username"))
                .setHot(longValue(item, "replies"))
                .setUrl(text(item, "url"))
                .setMobileUrl(text(item, "url"))));
    }

    private DailyHotResult fetchWeatherAlarm(DailyHotSourceEnum source) {
        return fetchJsonArray(source,
                "http://www.nmc.cn/rest/findAlarm?pageNo=1&pageSize=20&signaltype=&signallevel=&province=",
                "data.page.list", (item, index) -> {
                    String url = "http://nmc.cn" + text(item, "url");
                    return new DailyHotItem()
                            .setId(firstTextAt(item, "id", "identifier"))
                            .setTitle(text(item, "title"))
                            .setDesc(text(item, "signaltype"))
                            .setTimestamp(parseTime(text(item, "effective")))
                            .setUrl(url)
                            .setMobileUrl(url);
                });
    }

    private DailyHotResult fetchWeread(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://weread.qq.com/web/bookListInCategory/rising?rank=1",
                defaultHeaders(), "books", (item, index) -> {
                    JsonNode book = item.path("bookInfo");
                    String id = text(book, "bookId");
                    String wereadId = wereadId(id);
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(book, "title"))
                            .setAuthor(text(book, "author"))
                            .setDesc(text(book, "intro"))
                            .setCover(StringUtils.defaultString(text(book, "cover")).replace("s_", "t9_"))
                            .setHot(longValue(item, "readingCount"))
                            .setTimestamp(parseTime(text(book, "publishTime")))
                            .setUrl("https://weread.qq.com/web/bookDetail/" + wereadId)
                            .setMobileUrl("https://weread.qq.com/web/bookDetail/" + wereadId);
                });
    }

    private DailyHotResult fetchYystv(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://www.yystv.cn/home/get_home_docs_by_page",
                "data", (item, index) -> {
                    String id = text(item, "id");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "title"))
                            .setDesc(text(item, "desc"))
                            .setCover(text(item, "cover"))
                            .setHot(longValue(item, "like_count"))
                            .setUrl("https://www.yystv.cn/p/" + id)
                            .setMobileUrl("https://www.yystv.cn/p/" + id);
                });
    }

    private DailyHotResult fetchZhihuDaily(DailyHotSourceEnum source) {
        return fetchJsonArray(source, "https://daily.zhihu.com/api/4/news/latest",
                headers("Referer", "https://daily.zhihu.com/api/4/news/latest"),
                "stories", (item, index) -> {
                    if (longValue(item, "type") != null && longValue(item, "type") != 0) {
                        return null;
                    }
                    String id = text(item, "id");
                    return new DailyHotItem()
                            .setId(id)
                            .setTitle(text(item, "title"))
                            .setCover(textAt(item, "images.0"))
                            .setUrl("https://daily.zhihu.com/story/" + id)
                            .setMobileUrl("https://daily.zhihu.com/story/" + id);
                });
    }

    private DailyHotResult fetchRss(DailyHotSourceEnum source, String url, String baseUrl) {
        DailyHotHttpResponse response = dailyHotHttpClient.get(url, defaultHeaders());
        return result(source, parseRss(response.getBody(), baseUrl));
    }

    private Map<String, String> defaultHeaders() {
        return headers("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/123.0.0.0 Safari/537.36");
    }

    private Map<String, String> githubHeaders() {
        return headers(
                "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "Accept-Language", "en-US,en;q=0.5",
                "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/123.0.0.0 Safari/537.36"
        );
    }

    private Map<String, String> coolapkHeaders() {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("X-App-Id", "com.coolapk.market");
        headers.put("X-App-Token", coolapkToken());
        headers.put("X-Sdk-Int", "29");
        headers.put("X-Sdk-Locale", "zh-CN");
        headers.put("X-App-Version", "11.0");
        headers.put("X-Api-Version", "11");
        headers.put("X-App-Code", "2101202");
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 10; Mi 10) AppleWebKit/537.36 Chrome/111.0.5563.15 Mobile Safari/537.36");
        return headers;
    }

    private Map<String, String> headers(String... values) {
        Map<String, String> headers = new LinkedHashMap<>();
        for (int i = 0; i + 1 < values.length; i += 2) {
            headers.put(values[i], values[i + 1]);
        }
        return headers;
    }

    private String coolapkToken() {
        String deviceId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis() / 1000;
        String hexNow = "0x" + Long.toHexString(now);
        String md5Now = md5(String.valueOf(now));
        String raw = "token://com.coolapk.market/c67ef5943784d09750dcfbb31020f0ab?" + md5Now + "$" + deviceId + "&com.coolapk.market";
        String encoded = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        return md5(encoded) + deviceId + hexNow;
    }

    private String md5(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            String hex = new BigInteger(1, bytes).toString(16);
            return "0".repeat(Math.max(0, 32 - hex.length())) + hex;
        } catch (Exception e) {
            throw new IllegalStateException("MD5计算失败", e);
        }
    }

    private String firstNumber(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        Matcher matcher = NUMBER_PATTERN.matcher(value);
        return matcher.find() ? matcher.group() : null;
    }

    private String ithomeDesktopLink(String url) {
        String id = firstNumber(url);
        if (StringUtils.isBlank(id) || id.length() < 4) {
            return url;
        }
        return "https://www.ithome.com/0/" + id.substring(0, 3) + "/" + id.substring(3) + ".htm";
    }

    private String ithomeMobileLink(String url) {
        String id = firstNumber(url);
        return StringUtils.isBlank(id) ? url : "https://m.ithome.com/html/" + id + ".htm";
    }

    private String decodeUrl(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        try {
            return java.net.URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value;
        }
    }

    private String wereadId(String bookId) {
        if (StringUtils.isBlank(bookId)) {
            return bookId;
        }
        try {
            String hash = md5(bookId);
            StringBuilder builder = new StringBuilder(hash.substring(0, 3));
            List<String> chunks = new ArrayList<>();
            if (bookId.matches("^\\d*$")) {
                builder.append("3");
                for (int i = 0; i < bookId.length(); i += 9) {
                    String chunk = bookId.substring(i, Math.min(i + 9, bookId.length()));
                    chunks.add(Long.toHexString(Long.parseLong(chunk)));
                }
            } else {
                builder.append("4");
                StringBuilder hex = new StringBuilder();
                for (char ch : bookId.toCharArray()) {
                    hex.append(Integer.toHexString(ch));
                }
                chunks.add(hex.toString());
            }
            builder.append("2").append(hash.substring(hash.length() - 2));
            for (int i = 0; i < chunks.size(); i++) {
                String chunk = chunks.get(i);
                String length = Integer.toHexString(chunk.length());
                builder.append(length.length() == 1 ? "0" + length : length).append(chunk);
                if (i < chunks.size() - 1) {
                    builder.append("g");
                }
            }
            if (builder.length() < 20) {
                builder.append(hash, 0, 20 - builder.length());
            }
            builder.append(md5(builder.toString()), 0, 3);
            return builder.toString();
        } catch (Exception e) {
            return bookId;
        }
    }
}
