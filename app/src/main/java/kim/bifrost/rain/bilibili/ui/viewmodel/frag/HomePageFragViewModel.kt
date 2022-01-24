package kim.bifrost.rain.bilibili.ui.viewmodel.frag

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kim.bifrost.coldrain.wanandroid.base.BasePagingSource
import kim.bifrost.rain.bilibili.model.web.ApiService

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.frag.HomePageFragViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/19 14:08
 **/
class HomePageFragViewModel : ViewModel() {
    val recommendList = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        pagingSourceFactory = {
            BasePagingSource {
                ApiService.getRecommends(page = it).list
            }
        }
    ).flow
}