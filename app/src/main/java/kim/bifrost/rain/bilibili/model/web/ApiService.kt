package kim.bifrost.rain.bilibili.model.web

import android.util.Log
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.model.web.bean.*
import kim.bifrost.rain.bilibili.utils.decompress
import kim.bifrost.rain.bilibili.utils.sign
import kim.bifrost.rain.retrofit.annotation.*
import okhttp3.*
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.zip.DeflaterInputStream
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * kim.bifrost.rain.bilibili.model.web.ApiService
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/19 12:10
 **/
@Suppress("SpellCheckingInspection")
interface ApiService {

    /**
     * 获取首页推荐
     * 过时
     *
     * @param tid 非必要
     * @param page 页数
     * @param pageSize 单页大小
     * @param order 排列方式
     * @return
     */
    @Deprecated("过时的API", replaceWith = ReplaceWith("use homePage()"))
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
        @Query("access_key") access_key: String? = App.accessToken,
        @Query("avid") avid: Int? = null,
        @Query("bvid") bvid: String? = null,
        @Query("cid") cid: Int,
        @Query("qn") qn: Int? = 64,
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

    /**
     * 请求二维码url及扫码密钥
     * 密钥超时时间为180s
     * 用了tv端的接口，以APP方式鉴权
     * 即access_token/refresh_token方式
     * appkey: 4409e2ce8ffd12b8 (tv端)
     * 盐值: 59b43e04ad6965f34319062b478f83dd
     *
     * @param appkey appkey
     * @param ts 时间戳
     * @param sign 签名 使用除sign外所有参数的url字串后连接相对应的盐值进行md5校验
     */
    @POST("http://passport.bilibili.com/x/passport-tv-login/qrcode/auth_code")
    @FormUrlEncoded
    suspend fun requestQRCode(
        @Field("appkey") appkey: String = "4409e2ce8ffd12b8",
        @Field("local_id") local_id: String = "0",
        @Field("ts") ts: Long = System.currentTimeMillis(),
        @Field("sign") sign: String = sign("appkey" to appkey, "local_id" to local_id, "ts" to ts.toString(), salt = "59b43e04ad6965f34319062b478f83dd")
    ): QrCodeRequestBean

    /**
     * 登录后用得到的密钥请求以获取access_token与refresh_token
     *
     * @param appkey
     * @param auth_code 密钥
     * @param local_id
     * @param ts
     * @param sign
     */
    @POST("http://passport.bilibili.com/x/passport-tv-login/qrcode/poll")
    @FormUrlEncoded
    suspend fun responseQRCode(
        @Field("appkey") appkey: String = "4409e2ce8ffd12b8",
        @Field("auth_code") auth_code: String,
        @Field("local_id") local_id: String = "0",
        @Field("ts") ts: Long = System.currentTimeMillis(),
        @Field("sign") sign: String = sign(
            "appkey" to appkey,
            "auth_code" to auth_code,
            "local_id" to local_id,
            "ts" to ts.toString(),
            salt = "59b43e04ad6965f34319062b478f83dd"
        )
    ): QrCodeResponseBean

    /**
     * 获取用户登录基本信息
     * 需要鉴权
     *
     * @param access_key
     * @param appkey
     * @param ts
     * @param sign
     * @return
     */
    @GET("http://app.bilibili.com/x/v2/account/myinfo")
    suspend fun getLoginUserInfo(
        @Query("access_key") access_key: String = App.accessToken.toString(),
        @Query("appkey") appkey: String = "4409e2ce8ffd12b8",
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("sign") sign: String = sign(
            "access_key" to access_key,
            "appkey" to appkey,
            "ts" to ts.toString(),
            salt = "59b43e04ad6965f34319062b478f83dd"
        )
    ): LoginUserData

    /**
     * 点赞
     *
     * @param access_key
     * @param aid av号
     * @param like 0点赞 1取消点赞
     * @return
     */
    @POST("https://app.bilibili.com/x/v2/view/like")
    @FormUrlEncoded
    suspend fun like(
        @Field("access_key") access_key: String = App.accessToken.toString(),
        @Field("aid") aid: Int,
        @Field("like") like: Int
    ): LikeResponse

    /**
     * 检查视频是否已被点赞
     *
     * @param access_key
     * @param aid av号
     * @param bvid bv号
     * @return
     */
    @GET("x/web-interface/archive/has/like")
    suspend fun isLike(
        @Query("access_key") access_key: String = App.accessToken.toString(),
        @Query("aid") aid: Int? = null,
        @Query("bvid") bvid: String? = null
    ): IsLikeResponse

    /**
     * 投币
     *
     * @param access_key
     * @param aid av号
     * @param multiply 投币数量，上限为2
     * @param select_like 是否点赞 默认为0，不点赞
     * @return
     */
    @POST("https://app.bilibili.com/x/v2/view/coin/add")
    @FormUrlEncoded
    suspend fun coin(
        @Field("access_key") access_key: String = App.accessToken.toString(),
        @Field("aid") aid: Int,
        @Field("multiply") multiply: Int,
        @Field("select_like") select_like: Int = 0
    ): CoinResponse

    /**
     * 判断视频已经投币的数量
     *
     * @param access_key
     * @param aid
     * @param bvid
     * @return
     */
    @GET("x/web-interface/archive/coins")
    suspend fun isCoin(
        @Query("access_key") access_key: String = App.accessToken.toString(),
        @Query("aid") aid: Int? = null,
        @Query("bvid") bvid: String? = null
    ): IsCoinResponse

    /**
     * 收藏
     * errorCode
     *   0：成功
     *   -101：账号未登录
     *   -111：csrf校验失败
     *   -400：请求错误
     *   -403：访问权限不足
     *   10003：不存在该稿件
     *   11201：已经收藏过了
     *   11202：已经取消收藏了
     *   11203：达到收藏上限
     *   72010017：参数错误
     *
     * @param access_key
     * @param aid
     * @param type
     * @param add_media_ids 同时添加多个，用 ,（%2C）分隔
     * @param del_media_ids 同时取消多个，用 ,（%2C）分隔
     */
    @POST("x/v3/fav/resource/deal")
    @FormUrlEncoded
    suspend fun collect(
        @Field("access_key") access_key: String = App.accessToken.toString(),
        @Field("rid") aid: Int,
        @Field("type") type: Int = 2,
        @Field("add_media_ids") add_media_ids: String? = null,
        @Field("del_media_ids") del_media_ids: String? = null
    ): CollectResponse

    /**
     * 是否收藏
     *
     * @param access_key
     * @param aid avid或bvid
     */
    @GET("x/v2/fav/video/favoured")
    suspend fun isCollect(
        @Query("access_key") access_key: String = App.accessToken.toString(),
        @Query("aid") aid: Int
    ): IsCollectResponse

    /**
     * 一键三连
     *
     * @param access_key
     * @param aid avid
     * @return
     */
    @POST("https://app.bilibili.com/x/v2/view/like/triple")
    @FormUrlEncoded
    suspend fun allLike(
        @Field("access_key") access_key: String = App.accessToken.toString(),
        @Field("aid") aid: Int? = null
    ): AllLikeResponse

    /**
     * 获取所有分区
     *
     * @param appkey
     * @param build
     * @param ts
     * @return
     */
    @GET("https://app.bilibili.com/x/v2/region")
    suspend fun getRegions(
        @Query("appkey") appkey: String = "4409e2ce8ffd12b8",
        @Query("build") build: Int = 505000,
        @Query("ts") ts: Long = System.currentTimeMillis() / 1000
    ): RegionResponse

    /**
     * 获取分区数据
     * Banner/Recommend/New
     *
     * @param build
     * @param mobi_app
     * @param platform
     * @param rid 分区rid
     * @return
     */
    @GET("https://app.bilibili.com/x/v2/region/dynamic")
    suspend fun getRegionData(
        @Query("build") build: Int = 5410000,
        @Query("mobi_app") mobi_app: String = "android",
        @Query("platform") platform: String = "android",
        @Query("rid") rid: Int
    ): RegionDataResponse

    /**
     * 读取更多分区内容
     * 一次返回10个视频
     * 配合paging实现分页加载
     *
     * @param build
     * @param pull
     * @param platform
     * @param rid 分区rid
     * @param ctime
     */
    @Deprecated(
        message = """
            由于没有找到他指定返回个数的参数，所以只能返回固定的十个。而Paging在单页个数不够的情况下会主动加载多页。
            这时传入的时间戳是一样的，我猜测他是采用了queryParams里的ctime作为seed取了一组随机数据，而seed相同的后果是返回的内容相同，导致了分区数据的重复。
            为了更好的兼容Paging，使用web端接口 /x/web-interface/newlist
        """,
        replaceWith = ReplaceWith("getListData")
    )
    @GET("https://app.bilibili.com/x/v2/region/dynamic/list")
    suspend fun regionLoadMore(
        @Query("build") build: Int = 5400000,
        @Query("pull") pull: Boolean = false,
        @Query("platform") platform: String = "android",
        @Query("rid") rid: Int,
        @Query("ps") ps: Int = 30,
        @Query("ctime") ctime: Long = System.currentTimeMillis()
    ): RegionContentResponse

    /**
     * 请求首页数据
     *
     * @param access_key
     * @param adExtra
     * @param autoplayCard
     * @param bannerHash
     * @param column
     * @param deviceType
     * @param flush
     * @param fnVal
     * @param fnVer
     * @param forceHost
     * @param index
     * @param loginEvent
     * @param network
     * @param openEvent
     * @param pull 是否是新请求的数据
     * @param qn
     * @param recsysMode
     * @return
     */
    @GET("https://app.bilibili.com/x/v2/feed/index")
    suspend fun homePage(
        @Query("access_key") access_key: String? = App.accessToken,
        @Query("ad_extra") adExtra: String? = null,
        @Query("autoplay_card") autoplayCard: Int = 0,
        @Query("banner_hash") bannerHash: String? = null,
        @Query("column") column: Int = 3,
        @Query("device_type") deviceType: Int = 0,
        @Query("flush") flush: Int = 0,
        @Query("fnval") fnVal: Int = 16,
        @Query("fnver") fnVer: Int = 0,
        @Query("force_host") forceHost: Int = 0,
        @Query("idx") index: Long = System.currentTimeMillis(),
        @Query("login_event") loginEvent: Int = 0,
        @Query("network") network: String = "mobile",
        @Query("open_event") openEvent: String? = null,
        @Query("pull") pull: Boolean = true,
        @Query("qn") qn: Int = 32,
        @Query("recsys_mode") recsysMode: Int = 0
    ): HomePage

    /**
     * 获取用户信息
     * 由于不能鉴权，部分信息无法获取
     *
     * @param mid
     */
    @GET("x/space/acc/info")
    suspend fun getUserInfo(
        @Query("mid") mid: Int
    ): UserInfo

    /**
     * 获取用户卡片信息
     *
     * @param mid
     * @param photo
     * @return
     */
    @GET("x/web-interface/card")
    suspend fun getUserCard(
        @Query("mid") mid: Int,
        @Query("photo") photo: Boolean
    ): UserCardInfo

    /**
     * web端接口
     * 分页获取指定分区的数据
     * 该接口为浏览器抓包获取，有些参数不知道什么意思
     *
     * @param rid region ID
     * @param type
     * @param jsonp
     * @param pageNumber 第几页，从1开始
     * @param pageSize 每页数量
     */
    @GET("x/web-interface/newlist")
    suspend fun getListData(
        @Query("rid") rid: Int,
        @Query("type") type: Int = 0,
        @Query("jsonp") jsonp: String = "jsonp",
        @Query("pn") pageNumber: Int,
        @Query("ps") pageSize: Int = 20
    ): PagerListBean

    companion object : ApiService by RetrofitHelper.service {

        private val client by lazy { OkHttpClient.Builder().build() }

        /**
         * 获取xml弹幕
         * 回调转为协程的实践
         *
         * @param cid
         * @return
         */
        suspend fun getXmlDanmaku(cid: Int): ByteArray? = suspendCoroutine {
            client.newCall(
                Request.Builder()
                    .url("http://comment.bilibili.com/$cid.xml")
                    .build()
            ).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    it.resumeWith(Result.success(response.body?.bytes()?.decompress()))
                }
            })
        }

    }
}

interface ProtoBufService {

    companion object : ProtoBufService by RetrofitHelper.protoBufService
}