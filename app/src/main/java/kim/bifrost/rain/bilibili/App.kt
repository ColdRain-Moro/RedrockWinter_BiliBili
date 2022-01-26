package kim.bifrost.rain.bilibili

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.text.DateFormat

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

        private val searchHistoryData: SharedPreferences by lazy { context.getSharedPreferences("search_history", Context.MODE_PRIVATE) }

        fun addSearchHistory(query: String) {
            searchHistoryData.edit {
                putStringSet(
                    "search_history",
                    searchHistoryData
                        .getStringSet("search_history", emptySet())!!
                        .toMutableSet()
                        .apply {
                            add(query)
                        }
                )
            }
        }
    }
}