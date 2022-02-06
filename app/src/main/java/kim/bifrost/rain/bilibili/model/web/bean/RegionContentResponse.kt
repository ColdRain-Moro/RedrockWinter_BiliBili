package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class RegionContentResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val cbottom: Int,
        val ctop: Int,
        val new: List<New>
    ) : Serializable {
        data class New(
            val cover: String,
            val danmaku: Int,
            val duration: Int,
            val face: String,
            val favourite: Int,
            val goto: String,
            val like: Int,
            val name: String,
            val `param`: String,
            val play: Int,
            val pubdate: Int,
            val reply: Int,
            val rid: Int,
            val rname: String,
            val title: String,
            val uri: String
        ) : Serializable
    }
}