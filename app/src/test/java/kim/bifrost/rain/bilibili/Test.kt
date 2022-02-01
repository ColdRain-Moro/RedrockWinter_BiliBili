package kim.bifrost.rain.bilibili

import kim.bifrost.rain.bilibili.model.web.ApiService
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoPlayData
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter

/**
 * kim.bifrost.rain.bilibili.Test
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/22 0:13
 **/
suspend fun main() {
    val bean = ApiService.requestQRCode()
    println(bean)
}