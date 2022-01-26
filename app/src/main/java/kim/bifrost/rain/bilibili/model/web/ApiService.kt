package kim.bifrost.rain.bilibili.model.web

import kim.bifrost.rain.bilibili.model.web.bean.*
import kim.bifrost.rain.retrofit.annotation.GET
import kim.bifrost.rain.retrofit.annotation.Query

/**
 * kim.bifrost.rain.bilibili.model.web.ApiService
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/19 12:10
 **/
interface ApiService {

    /**
     * 获取首页推荐
     *
     * @param tid 非必要
     * @param page 页数
     * @param pageSize 单页大小
     * @param order 排列方式
     * @return
     */
    @GET("recommend")
    suspend fun getRecommends(
        @Query("tid") tid: Int? = null,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int = 30,
        @Query("order") order: String = "default",
    ): RecommendBean

    /**
     * 获取视频播放流url
     *
     * @param avid avid 与bvid任选一个
     * @param bvid bvid
     * @param cid cid
     * @param qn 视频清晰度选择 未登录默认32 (480p) 登录后默认64 （720p)
     * @param fnval 视频流格式标识 二进制属性位 活用位运算
     * @param fnver 固定为0，作用未知
     * @param fourk 是否允许4k视频 (默认0，画质最高1080p:0，最高4k:1)
     * @return
     */
    @GET("x/player/playurl")
    suspend fun getVideo(
        @Query("avid") avid: Int? = null,
        @Query("bvid") bvid: String? = null,
        @Query("cid") cid: Int,
        @Query("qn") qn: Int? = 32,
        @Query("fnval") fnval: Int? = null,
        @Query("fnver") fnver: Int? = null,
        @Query("fourk") fourk: Int? = null,
    ): VideoPlayData

    /**
     * 获取视频详细信息
     *
     * @param aid avid
     * @param bvid bvid
     * @return
     */
    @GET("x/web-interface/view")
    suspend fun getVideoInfo(
        @Query("aid") aid: Int? = null,
        @Query("bvid") bvid: String? = null,
    ): VideoInfo

    /**
     * 获取视频的相关推荐视频
     * 获取数量是定长40
     *
     * @param aid avid
     * @param bvid bvid
     */
    @GET("x/web-interface/archive/related")
    suspend fun getVideoRecommends(
        @Query("aid") aid: Int? = null,
        @Query("bvid") bvid: String? = null,
    ): VideoRecommends

    /**
     * 获取视频回复
     *
     * @param next 上一页数据中的下一页页码
     * @param aid avid
     * @param type 类型，目前作用未知，但为必要参数
     * @return
     */
    @GET("x/v2/reply/main")
    suspend fun getFeedBack(
        @Query("next") next: Int,
        @Query("oid") aid: Int,
        @Query("type") type: Int = 1,
        @Query("jsonp") jsonp: String = "jsonp",
    ): ReplyBean

    /**
     * 分类搜索
     * https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/search/search_request.md
     *
     * @param search_type 搜索目标类型
     * @param keyword 需要搜索的关键词
     * @param order 结果排序方式
     * @param order_sort 用户粉丝数及等级排序顺序
     * @param user_type 用户分类筛选
     * @param duration 视频时长筛选
     * @param tids 视频分区筛选
     * @param category_id 专栏及相簿分区筛选
     * @param page 页码
     * @return
     */
    @GET("x/web-interface/search/type")
    suspend fun search(
        @Query("search_type") search_type: String = "video",
        @Query("keyword") keyword: String,
        @Query("order") order: String = "totalrank",
        @Query("order_sort") order_sort: Int = 0,
        @Query("user_type") user_type: Int = 0,
        @Query("duration") duration: Int = 0,
        @Query("tids") tids: Int = 0,
        @Query("category_id") category_id: Int = 0,
        @Query("page") page: Int = 1,
    ): VideoSearchResultBean

    /**
     * 获取搜索建议
     *
     * @param term 关键词
     * @param sponly
     * @return
     */
    @GET("suggest")
    suspend fun searchSuggest(
        @Query("term") term: String,
        @Query("sponly") sponly: Int? = null,
    ): Map<Int, SearchSuggestion>

    /**
     * 获取搜索热词
     *
     * @return
     */
    @GET("http://s.search.bilibili.com/main/hotword")
    suspend fun hotkey(): HotKeyBean

    companion object : ApiService by RetrofitHelper.service
}

interface ProtoBufService {

    companion object : ProtoBufService by RetrofitHelper.protoBufService
}