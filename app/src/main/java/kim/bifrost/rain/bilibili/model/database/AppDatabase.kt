package kim.bifrost.rain.bilibili.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kim.bifrost.rain.bilibili.App

/**
 * kim.bifrost.rain.bilibili.model.database.AppDatabase
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/2 0:17
 **/
@Database(entities = [WatchHistoryVideoBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getHistoryWatchDao(): HistoryWatchDao

    companion object {

        val impl by lazy { create(App.context) }

        private fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "bilibili_database")
                .build()
    }
}