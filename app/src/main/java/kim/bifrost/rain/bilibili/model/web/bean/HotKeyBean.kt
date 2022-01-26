package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class HotKeyBean(
    val code: Int,
    val cost: Cost,
    val exp_str: String,
    val list: List<HotKey>,
    val message: String,
    val seid: String,
    val timestamp: Int
) : Serializable {
    data class Cost(
        val deserialize_response: String,
        val main_handler: String,
        val params_check: String,
        val reas_request: String,
        val reas_request_format: String,
        val reas_response_format: String,
        val total: String
    ) : Serializable

    data class HotKey(
        val goto_type: Int,
        val goto_value: String,
        val hot_id: Int,
        val icon: String,
        val id: Int,
        val keyword: String,
        val name_type: String,
        val pos: Int,
        val resource_id: Int,
        val show_name: String,
        val status: String,
        val word_type: Int
    ) : Serializable
}