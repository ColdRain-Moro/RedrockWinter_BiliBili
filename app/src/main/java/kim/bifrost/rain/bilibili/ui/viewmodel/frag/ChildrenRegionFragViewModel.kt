package kim.bifrost.rain.bilibili.ui.viewmodel.frag

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kim.bifrost.coldrain.wanandroid.base.BasePagingSource
import kim.bifrost.rain.bilibili.model.web.ApiService

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.frag.ChildrenRegionFragViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/5 20:29
 **/
class ChildrenRegionFragViewModel : ViewModel() {
    suspend fun getDataFlow(rid: Int) = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            BasePagingSource {
                ApiService.regionLoadMore(rid = rid, pull = false).data.new
            }
        }
    ).flow
}