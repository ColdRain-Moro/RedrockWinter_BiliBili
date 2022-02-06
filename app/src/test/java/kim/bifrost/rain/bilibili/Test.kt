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
    val data = ApiService.like(
        access_key = "434a6e6dcdc5ae1cd3e950f0919cf521",
        aid = 888300298,
        like = 0
    )
    val client = OkHttpClient.Builder().build()
    val body = FormBody.Builder()
        .add("access_key", "434a6e6dcdc5ae1cd3e950f0919cf521")
        .add("aid", "888300298")
        .add("like", "0")
        .build()
    println(client.newCall(Request.Builder().url("https://app.bilibili.com/x/v2/view/like").post(body).build()).execute().body?.string())
}