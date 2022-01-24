package kim.bifrost.rain.bilibili.model.web.bean
import java.io.Serializable


/**
 * kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/21 20:33
 **/
data class VideoInfo(
    val code: Int,
    val data: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val aid: Int,
        val bvid: String,
        val cid: Int,
        val copyright: Int,
        val ctime: Long,
        val desc: String,
        val desc_v2: List<DescV2>,
        val dimension: Dimension,
        val duration: Int,
        val `dynamic`: String,
        val honor_reply: HonorReply,
        val is_season_display: Boolean,
        val no_cache: Boolean,
        val owner: Owner,
        val pages: List<Page>,
        val pic: String,
        val pubdate: Long,
        val rights: Rights,
        val season_id: Int,
        val stat: Stat,
        val state: Int,
        val subtitle: Subtitle,
        val tid: Int,
        val title: String,
        val tname: String,
        val ugc_season: UgcSeason,
        val user_garb: UserGarb,
        val videos: Int
    ) : Serializable {
        data class DescV2(
            val biz_id: Int,
            val raw_text: String,
            val type: Int
        ) : Serializable

        data class Dimension(
            val height: Int,
            val rotate: Int,
            val width: Int
        ) : Serializable

        class HonorReply : Serializable

        data class Owner(
            val face: String,
            val mid: Int,
            val name: String
        ) : Serializable

        data class Page(
            val cid: Int,
            val dimension: Dimension,
            val duration: Int,
            val first_frame: String,
            val from: String,
            val page: Int,
            val part: String,
            val vid: String,
            val weblink: String
        ) : Serializable {
            data class Dimension(
                val height: Int,
                val rotate: Int,
                val width: Int
            ) : Serializable
        }

        data class Rights(
            val autoplay: Int,
            val bp: Int,
            val clean_mode: Int,
            val download: Int,
            val elec: Int,
            val hd5: Int,
            val is_360: Int,
            val is_cooperation: Int,
            val is_stein_gate: Int,
            val movie: Int,
            val no_background: Int,
            val no_reprint: Int,
            val no_share: Int,
            val pay: Int,
            val ugc_pay: Int,
            val ugc_pay_preview: Int
        ) : Serializable

        data class Stat(
            val aid: Int,
            val argue_msg: String,
            val coin: Int,
            val danmaku: Int,
            val dislike: Int,
            val evaluation: String,
            val favorite: Int,
            val his_rank: Int,
            val like: Int,
            val now_rank: Int,
            val reply: Int,
            val share: Int,
            val view: Int
        ) : Serializable

        data class Subtitle(
            val allow_submit: Boolean,
            val list: List<Any>
        ) : Serializable

        data class UgcSeason(
            val attribute: Int,
            val cover: String,
            val ep_count: Int,
            val id: Int,
            val intro: String,
            val mid: Int,
            val season_type: Int,
            val sections: List<Section>,
            val sign_state: Int,
            val stat: Stat,
            val title: String
        ) : Serializable {
            data class Section(
                val episodes: List<Episode>,
                val id: Int,
                val season_id: Int,
                val title: String,
                val type: Int
            ) : Serializable {
                data class Episode(
                    val aid: Int,
                    val arc: Arc,
                    val attribute: Int,
                    val bvid: String,
                    val cid: Int,
                    val id: Int,
                    val page: Page,
                    val season_id: Int,
                    val section_id: Int,
                    val title: String
                ) : Serializable {
                    data class Arc(
                        val aid: Int,
                        val author: Author,
                        val copyright: Int,
                        val ctime: Int,
                        val desc: String,
                        val desc_v2: Any,
                        val dimension: Dimension,
                        val duration: Int,
                        val `dynamic`: String,
                        val pic: String,
                        val pubdate: Int,
                        val rights: Rights,
                        val stat: Stat,
                        val state: Int,
                        val title: String,
                        val type_id: Int,
                        val type_name: String,
                        val videos: Int
                    ) : Serializable {
                        data class Author(
                            val face: String,
                            val mid: Int,
                            val name: String
                        ) : Serializable

                        data class Dimension(
                            val height: Int,
                            val rotate: Int,
                            val width: Int
                        ) : Serializable

                        data class Rights(
                            val autoplay: Int,
                            val bp: Int,
                            val download: Int,
                            val elec: Int,
                            val hd5: Int,
                            val is_cooperation: Int,
                            val movie: Int,
                            val no_reprint: Int,
                            val pay: Int,
                            val ugc_pay: Int,
                            val ugc_pay_preview: Int
                        ) : Serializable

                        data class Stat(
                            val aid: Int,
                            val argue_msg: String,
                            val coin: Int,
                            val danmaku: Int,
                            val dislike: Int,
                            val evaluation: String,
                            val fav: Int,
                            val his_rank: Int,
                            val like: Int,
                            val now_rank: Int,
                            val reply: Int,
                            val share: Int,
                            val view: Int
                        ) : Serializable
                    }

                    data class Page(
                        val cid: Int,
                        val dimension: Dimension,
                        val duration: Int,
                        val from: String,
                        val page: Int,
                        val part: String,
                        val vid: String,
                        val weblink: String
                    ) : Serializable {
                        data class Dimension(
                            val height: Int,
                            val rotate: Int,
                            val width: Int
                        ) : Serializable
                    }
                }
            }

            data class Stat(
                val coin: Int,
                val danmaku: Int,
                val fav: Int,
                val his_rank: Int,
                val like: Int,
                val now_rank: Int,
                val reply: Int,
                val season_id: Int,
                val share: Int,
                val view: Int
            ) : Serializable
        }

        data class UserGarb(
            val url_image_ani_cut: String
        ) : Serializable
    }
}