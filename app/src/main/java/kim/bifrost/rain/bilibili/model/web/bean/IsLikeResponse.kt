package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class IsLikeResponse(
    val code: Int,
    val `data`: Int,
    val message: String,
    val ttl: Int
) : Serializable