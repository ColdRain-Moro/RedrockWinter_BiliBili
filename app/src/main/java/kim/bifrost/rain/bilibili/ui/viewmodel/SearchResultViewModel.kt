package kim.bifrost.rain.bilibili.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kim.bifrost.coldrain.wanandroid.base.BasePagingSource
import kim.bifrost.rain.bilibili.model.web.ApiService

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.SearchResultViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/25 21:55
 **/
class SearchResultViewModel : ViewModel() {
    fun search(key: String) = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        pagingSourceFactory = {
            BasePagingSource {
                ApiService.search(
                    keyword = key,
                    page = it + 1
                ).data.result
            }
        }
    ).flow

    suspend fun hotkeys() = ApiService.hotkey().list.map { it.keyword }

    suspend fun suggestions(key: String) = ApiService.searchSuggest(key)
        .entries
        .sortedBy { it.key }
        .map { it.value.value }
}