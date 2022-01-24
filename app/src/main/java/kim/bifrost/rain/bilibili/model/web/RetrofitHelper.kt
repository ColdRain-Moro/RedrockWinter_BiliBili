package kim.bifrost.rain.bilibili.model.web

import kim.bifrost.rain.bilibili.utils.Constant
import kim.bifrost.rain.retrofit.GsonConverter
import kim.bifrost.rain.retrofit.RainRetrofit
import okhttp3.OkHttpClient

/**
 * kim.bifrost.rain.bilibili.model.web.RetrofitHelper
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/19 22:19
 **/
object RetrofitHelper {

    private lateinit var retrofit: RainRetrofit
    private lateinit var protobufRetrofit: RainRetrofit
    internal val service by lazy { getRetrofit().create(ApiService::class.java) }
    internal val protoBufService by lazy { getProtoBufRetrofit().create(ProtoBufService::class.java) }

    private fun getRetrofit(): RainRetrofit {
        if (!::retrofit.isInitialized) {
            retrofit = RainRetrofit.Builder()
                .baseurl(Constant.BASE_URL)
                .converter(GsonConverter.create())
                .okhttpClient(getOkHttpClient())
                .build()
        }
        return retrofit
    }

    private fun getProtoBufRetrofit(): RainRetrofit {
        if (!::protobufRetrofit.isInitialized) {
            protobufRetrofit = RainRetrofit.Builder()
                .baseurl(Constant.BASE_URL)
                .converter(ProtoBufConverter())
                .okhttpClient(getOkHttpClient())
                .build()
        }
        return protobufRetrofit
    }

    private fun getOkHttpClient(): OkHttpClient {
        // 这里可以加拦截器
        // 在拦截器里带上鉴权信息
        return OkHttpClient.Builder()
            .build()
    }
}