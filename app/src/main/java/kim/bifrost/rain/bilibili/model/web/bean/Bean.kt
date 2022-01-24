package kim.bifrost.rain.bilibili.model.web.bean

/**
 * kim.bifrost.rain.bilibili.model.web.Bean
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/20 14:39
 **/
data class RecommendBean(
    val code: Int,
    val list: List<SimpleVideoInfo>,
    val num: Int,
    val pages: Int
)

data class SimpleVideoInfo(
    val aid: Int,
    val author: String,
    val bvid: String,
    val coins: Int,
    val create: String,
    val credit: Int,
    val description: String,
    val duration: String,
    val favorites: Int,
    val last_recommend: List<LastRecommend>,
    val like: Int,
    val mid: Int,
    val pic: String,
    val play: Int,
    val review: Int,
    val rights: Rights,
    val subtitle: String,
    val title: String,
    val typeid: Int,
    val typename: String,
    val video_review: Int
) {
    data class LastRecommend(
        val face: String,
        val mid: Int,
        val msg: String,
        val time: Int,
        val uname: String
    )

    data class Rights(
        val autoplay: Int,
        val bp: Int,
        val download: Int,
        val elec: Int,
        val hd5: Int,
        val is_cooperation: Int,
        val movie: Int,
        val no_background: Int,
        val no_reprint: Int,
        val pay: Int,
        val ugc_pay: Int,
        val ugc_pay_preview: Int
    )
}

data class VideoPlayData(
    val code: Int,
    val data: Data,
    val message: String,
    val ttl: Int
) {
    data class Data(
        val accept_description: List<String>,
        val accept_format: String,
        val accept_quality: List<Int>,
        val durl: List<Durl>,
        val format: String,
        val from: String,
        val high_format: Any,
        val message: String,
        val quality: Int,
        val result: String,
        val seek_param: String,
        val seek_type: String,
        val support_formats: List<SupportFormat>,
        val timelength: Int,
        val video_codecid: Int
    )

    data class Durl(
        val ahead: String,
        val backup_url: List<String>,
        val length: Int,
        val order: Int,
        val size: Int,
        val url: String,
        val vhead: String
    )

    data class SupportFormat(
        val codecs: Any,
        val display_desc: String,
        val format: String,
        val new_description: String,
        val quality: Int,
        val superscript: String
    )
}