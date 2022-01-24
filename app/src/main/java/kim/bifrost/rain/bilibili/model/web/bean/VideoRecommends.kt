package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class VideoRecommends(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) : Serializable {
    data class Data(
        val aid: Int,
        val bvid: String,
        val cid: Int,
        val copyright: Int,
        val ctime: Int,
        val desc: String,
        val dimension: Dimension,
        val duration: Int,
        val `dynamic`: String,
        val first_frame: String,
        val is_ogv: Boolean,
        val mission_id: Int,
        val ogv_info: Any,
        val owner: Owner,
        val pic: String,
        val pubdate: Int,
        val rcmd_reason: String,
        val rights: Rights,
        val season_id: Int,
        val season_type: Int,
        val short_link: String,
        val short_link_v2: String,
        val stat: Stat,
        val state: Int,
        val tid: Int,
        val title: String,
        val tname: String,
        val up_from_v2: Int,
        val videos: Int
    ) : Serializable {
        data class Dimension(
            val height: Int,
            val rotate: Int,
            val width: Int
        ) : Serializable

        data class Owner(
            val face: String,
            val mid: Int,
            val name: String
        ) : Serializable

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
        ) : Serializable

        data class Stat(
            val aid: Int,
            val coin: Int,
            val danmaku: Int,
            val dislike: Int,
            val favorite: Int,
            val his_rank: Int,
            val like: Int,
            val now_rank: Int,
            val reply: Int,
            val share: Int,
            val view: Int
        ) : Serializable
    }
}