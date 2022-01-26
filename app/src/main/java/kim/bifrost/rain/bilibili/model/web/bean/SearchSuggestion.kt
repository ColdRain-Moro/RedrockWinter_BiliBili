package kim.bifrost.rain.bilibili.model.web.bean

/**
 * kim.bifrost.rain.bilibili.model.web.bean.SearchSuggestion
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/26 11:43
 **/
data class SearchSuggestion(
    val value: String,
    val term: String,
    val ref: Int,
    val name: String,
    val spid: Int
)