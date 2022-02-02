package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class IsCollectResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val count: Int,
        val favoured: Boolean
    ) : Serializable
}