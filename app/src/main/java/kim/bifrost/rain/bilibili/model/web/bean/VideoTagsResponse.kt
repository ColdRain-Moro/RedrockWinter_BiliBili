package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class VideoTagsResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val attribute: Int,
        val content: String,
        val count: Count,
        val cover: String,
        val ctime: Int,
        val extra_attr: Int,
        val hated: Int,
        val hates: Int,
        val head_cover: String,
        val is_atten: Int,
        val liked: Int,
        val likes: Int,
        val short_content: String,
        val state: Int,
        val tag_id: Int,
        val tag_name: String,
        val type: Int
    ) : Serializable {
        data class Count(
            val atten: Int,
            val use: Int,
            val view: Int
        ) : Serializable
    }
}