package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class VideoSearchResultBean(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val cost_time: CostTime,
        val egg_hit: Int,
        val numPages: Int,
        val numResults: Int,
        val page: Int,
        val pagesize: Int,
        val result: List<Result>,
        val rqt_type: String,
        val seid: String,
        val show_column: Int,
        val suggest_keyword: String
    ) : Serializable {
        data class CostTime(
            val as_request: String,
            val as_request_format: String,
            val as_response_format: String,
            val deserialize_response: String,
            val illegal_handler: String,
            val main_handler: String,
            val params_check: String,
            val save_cache: String,
            val total: String
        ) : Serializable

        data class Result(
            val aid: Int,
            val arcrank: String,
            val arcurl: String,
            val author: String,
            val badgepay: Boolean,
            val bvid: String,
            val corner: String,
            val cover: String,
            val desc: String,
            val description: String,
            val duration: String,
            val favorites: Int,
            val hit_columns: List<String>,
            val id: Int,
            val is_pay: Int,
            val is_union_video: Int,
            val like: Int,
            val mid: Int,
            val pic: String,
            val play: Int,
            val pubdate: Int,
            val rank_score: Int,
            val rec_reason: String,
            val review: Int,
            val senddate: Int,
            val tag: String,
            val title: String,
            val type: String,
            val typeid: String,
            val typename: String,
            val upic: String,
            val url: String,
            val video_review: Int,
            val view_type: String
        ) : Serializable
    }
}