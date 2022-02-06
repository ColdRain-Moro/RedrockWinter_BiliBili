package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class UserInfo(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val birthday: String,
        val coins: Int,
        val face: String,
        val fans_badge: Boolean,
        val fans_medal: FansMedal,
        val is_followed: Boolean,
        val jointime: Int,
        val level: Int,
        val live_room: LiveRoom,
        val mid: Int,
        val moral: Int,
        val name: String,
        val nameplate: Nameplate,
        val official: Official,
        val pendant: Pendant,
        val profession: Profession,
        val rank: Int,
        val school: School,
        val series: Series,
        val sex: String,
        val sign: String,
        val silence: Int,
        val sys_notice: SysNotice,
        val tags: Any,
        val theme: Theme,
        val top_photo: String,
        val user_honour_info: UserHonourInfo,
        val vip: Vip
    ) : Serializable {
        data class FansMedal(
            val medal: Medal,
            val show: Boolean,
            val wear: Boolean
        ) : Serializable {
            data class Medal(
                val day_limit: Int,
                val intimacy: Int,
                val is_lighted: Int,
                val level: Int,
                val light_status: Int,
                val medal_color: Int,
                val medal_color_border: Int,
                val medal_color_end: Int,
                val medal_color_start: Int,
                val medal_id: Int,
                val medal_name: String,
                val next_intimacy: Int,
                val score: Int,
                val target_id: Int,
                val uid: Int,
                val wearing_status: Int
            ) : Serializable
        }

        data class LiveRoom(
            val broadcast_type: Int,
            val cover: String,
            val liveStatus: Int,
            val online: Int,
            val roomStatus: Int,
            val roomid: Int,
            val roundStatus: Int,
            val title: String,
            val url: String
        ) : Serializable

        data class Nameplate(
            val condition: String,
            val image: String,
            val image_small: String,
            val level: String,
            val name: String,
            val nid: Int
        ) : Serializable

        data class Official(
            val desc: String,
            val role: Int,
            val title: String,
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

        data class Profession(
            val name: String
        ) : Serializable

        data class School(
            val name: String
        ) : Serializable

        data class Series(
            val show_upgrade_window: Boolean,
            val user_upgrade_status: Int
        ) : Serializable

        class SysNotice : Serializable

        class Theme : Serializable

        data class UserHonourInfo(
            val colour: Any,
            val mid: Int,
            val tags: Any
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
}