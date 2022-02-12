package kim.bifrost.rain.bilibili.model.web.interceptor

import android.util.Log
import kim.bifrost.rain.bilibili.App
import okhttp3.Interceptor
import okhttp3.Response

/**
 * kim.bifrost.rain.bilibili.model.web.intercepter.RequestIntercepter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/30 13:09
 **/
class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build().also { Log.d("Test", "(RequestInterceptor.kt:17) ==> ${it.headers.toMultimap()}") })
    }
}