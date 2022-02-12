package kim.bifrost.rain.bilibili

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import xyz.doikki.videoplayer.ijk.IjkPlayerFactory
import xyz.doikki.videoplayer.player.AndroidMediaPlayerFactory
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager

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
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
            //使用使用IjkPlayer解码
            .setPlayerFactory(IjkPlayerFactory.create())
            //使用MediaPlayer解码
//            .setPlayerFactory(AndroidMediaPlayerFactory.create())
            .build())
    }

    companion object {

        // 弹幕屏蔽等级
        var danmakuShieldLevel = 5
        // 启用弹幕
        var enableDanmaku = true

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        val gson: Gson by lazy { GsonBuilder().create() }

        // access_token refresh_token date
        private val tokenData: SharedPreferences by lazy { context.getSharedPreferences("token", Context.MODE_PRIVATE) }

        val accessToken: String?
            get() = if (tokenData.getLong("date", 0).let { System.currentTimeMillis() < it })
                        tokenData.getString("access_token", null) else null

        val token_expires_date: Long
            get() = tokenData.getLong("date", 0)

        fun setToken(
            access_token: String?,
            refresh_token: String?,
            date: Long
        ) {
            tokenData.edit(commit = true) {
                remove("access_token")
                remove("refresh_token")
                remove("date")
                putString("access_token", access_token)
                putString("refresh_token", refresh_token)
                putLong("date", date)
            }
        }
    }
}