package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class AllLikeResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val coin: Boolean,
        val fav: Boolean,
        val like: Boolean,
        val multiply: Int
    ) : Serializable
}