package kim.bifrost.rain.bilibili.ui.viewmodel

import androidx.lifecycle.ViewModel
import kim.bifrost.rain.bilibili.model.database.AppDatabase

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.WatchHistoryViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/2 13:16
 **/
class WatchHistoryViewModel : ViewModel() {
    private val dao by lazy { AppDatabase.impl.getHistoryWatchDao() }
    fun getAllHistory() = dao.queryAll()
    fun search(query: String) = dao.search(query)
    fun removeAll() = dao.delete()
}