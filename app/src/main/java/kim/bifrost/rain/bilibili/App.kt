package kim.bifrost.rain.bilibili

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
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