package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class RegionResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val ttl: Int,
    val ver: String
) : Serializable {
    data class Data(
        val children: List<Children>,
        val goto: String,
        val is_bangumi: Int,
        val logo: String,
        val name: String,
        val param: String,
        val reid: Int,
        val tid: Int,
        val type: Int,
        val uri: String
    ) : Serializable {
        data class Children(
            val goto: String,
            val logo: String,
            val name: String,
            val `param`: String,
            val reid: Int,
            val tid: Int,
            val type: Int
        ) : Serializable
    }
}