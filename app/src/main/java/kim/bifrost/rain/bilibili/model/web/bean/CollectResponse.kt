package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class CollectResponse(
    val code: Int,
    val `data`: Data,
    val message: String
) : Serializable {
    data class Data(
        val prompt: Boolean
    ) : Serializable
}