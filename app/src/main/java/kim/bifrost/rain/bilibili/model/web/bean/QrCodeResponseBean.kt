package kim.bifrost.rain.bilibili.model.web.bean

/**
 * kim.bifrost.rain.bilibili.model.web.bean.QrCodeResponseBean
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/31 10:26
 **/
data class QrCodeResponseBean(
    val code: Int,
    val message: String,
    val ttl: Int,
    val data: Data?
) {
    data class Data(
        val mid: Int,
        val access_token: String,
        val refresh_token: String,
        val expires_in: Int
    )
}