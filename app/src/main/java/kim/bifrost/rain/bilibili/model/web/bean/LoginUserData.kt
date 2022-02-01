package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class LoginUserData(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val birthday: String,
        val coins: Double,
        val email_status: Int,
        val face: String,
        val identification: Int,
        val invite: Invite,
        val is_tourist: Int,
        val level: Int,
        val mid: Int,
        val name: String,
        val official: Official,
        val pin_prompting: Int,
        val rank: Int,
        val sex: Int,
        val sign: String,
        val silence: Int,
        val tel_status: Int,
        val vip: Vip
    ) : Serializable {
        data class Invite(
            val display: Boolean,
            val invite_remind: Int
        ) : Serializable

        data class Official(
            val desc: String,
            val role: Int,
            val title: String,
            val type: Int
        ) : Serializable

        data class Vip(
            val avatar_subscript: Int,
            val due_date: Long,
            val label: Label,
            val nickname_color: String,
            val status: Int,
            val theme_type: Int,
            val type: Int,
            val vip_pay_type: Int
        ) : Serializable {
            data class Label(
                val label_theme: String,
                val path: String,
                val text: String
            ) : Serializable
        }
    }
}