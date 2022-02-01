package kim.bifrost.rain.bilibili.model.web.bean

/**
 * kim.bifrost.rain.bilibili.model.web.bean.QrCodeRequestBean
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/30 18:29
 **/
data class QrCodeRequestBean(
    val code: Int,
    val message: String,
    val ttl: Int,
    val data: Data?
) {
    data class Data(
        val url: String,
        val auth_code: String
    )
}