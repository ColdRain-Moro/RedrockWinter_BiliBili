package kim.bifrost.rain.bilibili

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * kim.bifrost.rain.bilibili.App
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/16 15:02
 **/
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        val gson: Gson by lazy { GsonBuilder().create() }
    }
}