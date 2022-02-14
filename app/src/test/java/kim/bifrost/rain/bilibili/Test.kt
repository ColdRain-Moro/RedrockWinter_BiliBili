package kim.bifrost.rain.bilibili

import kim.bifrost.rain.bilibili.model.web.ApiService
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request


/**
 * kim.bifrost.rain.bilibili.Test
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/22 0:13
 **/
suspend fun main() {
    val data = ApiService.regionLoadMore(rid = 26)
}