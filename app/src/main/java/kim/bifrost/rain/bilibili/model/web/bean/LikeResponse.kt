package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class LikeResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val toast: String
    ) : Serializable
}