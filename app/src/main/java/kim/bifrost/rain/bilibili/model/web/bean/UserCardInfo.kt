package kim.bifrost.rain.bilibili.model.web.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserCardInfo(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val archive_count: Int,
        val article_count: Int,
        val card: Card,
        val follower: Int,
        val following: Boolean,
        val space: Space
    ) : Serializable {
        data class Card(
            val DisplayRank: String,
            @SerializedName("Official")
            val official: Official,
            val approve: Boolean,
            val article: Int,
            val attention: Int,
            val attentions: List<Any>,
            val birthday: String,
            val description: String,
            val face: String,
            val fans: Int,
            val friend: Int,
            val level_info: LevelInfo,
            val mid: String,
            val name: String,
            val nameplate: Nameplate,
            val official_verify: OfficialVerify,
            val pendant: Pendant,
            val place: String,
            val rank: String,
            val regtime: Int,
            val sex: String,
            val sign: String,
            val spacesta: Int,
            val vip: Vip
        ) : Serializable {
            data class Official(
                val desc: String,
                val role: Int,
                val title: String,
                val type: Int
            ) : Serializable

            data class LevelInfo(
                val current_exp: Int,
                val current_level: Int,
                val current_min: Int,
                val next_exp: Int
            ) : Serializable

            data class Nameplate(
                val condition: String,
                val image: String,
                val image_small: String,
                val level: String,
                val name: String,
                val nid: Int
            ) : Serializable

            data class OfficialVerify(
                val desc: String,
                val type: Int
            ) : Serializable

            data class Pendant(
                val expire: Int,
                val image: String,
                val image_enhance: String,
                val image_enhance_frame: String,
                val name: String,
                val pid: Int
            ) : Serializable

            data class Vip(
                val avatar_subscript: Int,
                val avatar_subscript_url: String,
                val due_date: Long,
                val label: Label,
                val nickname_color: String,
                val role: Int,
                val status: Int,
                val theme_type: Int,
                val type: Int,
                val vipStatus: Int,
                val vipType: Int,
                val vip_pay_type: Int
            ) : Serializable {
                data class Label(
                    val bg_color: String,
                    val bg_style: Int,
                    val border_color: String,
                    val label_theme: String,
                    val path: String,
                    val text: String,
                    val text_color: String
                ) : Serializable
            }
        }

        data class Space(
            val l_img: String,
            val s_img: String
        ) : Serializable
    }
}