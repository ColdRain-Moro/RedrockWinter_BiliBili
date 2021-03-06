package kim.bifrost.rain.bilibili.ui.viewmodel.frag

import android.util.Log
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

    val homePageFlow = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        ),
        pagingSourceFactory = {
            BasePagingSource {
                // 过滤广告和番剧，只保留small_cover_v2的视频
                // 番剧日后也许会适配 但广告给爷死
                ApiService.homePage(pull = false).data.items
                    .filter { s -> s.cardType == "small_cover_v2" || s.cardType == "large_cover_v1" }
            }
        }
    ).flow
}