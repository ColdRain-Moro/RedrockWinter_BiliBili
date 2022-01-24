package kim.bifrost.rain.bilibili.model.web

import kim.bifrost.rain.bilibili.model.web.bean.RecommendBean
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoPlayData
import kim.bifrost.rain.bilibili.model.web.bean.VideoRecommends
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
        @Query("order") order: String = "default"
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
        @Query("qn") qn: Int? = 16,
        @Query("fnval") fnval: Int? = null,
        @Query("fnver") fnver: Int? = null,
        @Query("fourk") fourk: Int? = null
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
        @Query("bvid") bvid: Int? = null
    ): VideoRecommends

    companion object : ApiService by RetrofitHelper.service
}

interface ProtoBufService {

    companion object : ProtoBufService by RetrofitHelper.protoBufService
}