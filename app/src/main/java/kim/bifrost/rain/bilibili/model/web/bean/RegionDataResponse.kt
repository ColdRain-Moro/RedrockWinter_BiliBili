package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class RegionDataResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val banner: Banner,
        val cbottom: Int,
        val ctop: Int,
        val new: List<New>,
        val recommend: List<Recommend>
    ) : Serializable {
        data class Banner(
            val top: List<Top>
        ) : Serializable {
            data class Top(
                val client_ip: String,
                val cm_mark: Int,
                val hash: String,
                val id: Int,
                val image: String,
                val index: Int,
                val is_ad: Boolean,
                val is_ad_loc: Boolean,
                val request_id: String,
                val resource_id: Int,
                val server_type: Int,
                val src_id: Int,
                val title: String,
                val uri: String
            ) : Serializable
        }

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

        data class Recommend(
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