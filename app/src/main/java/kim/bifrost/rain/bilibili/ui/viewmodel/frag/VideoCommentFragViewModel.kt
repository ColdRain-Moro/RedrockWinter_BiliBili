package kim.bifrost.rain.bilibili.ui.viewmodel.frag

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kim.bifrost.coldrain.wanandroid.base.BasePagingSource
import kim.bifrost.rain.bilibili.model.web.ApiService

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.frag.VideoCommentFragViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/23 11:08
 **/
class VideoCommentFragViewModel(private val aid: Int) : ViewModel() {
    val replies = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
        pagingSourceFactory = {
            BasePagingSource {
                val data = ApiService.getFeedBack(it + 1, aid)
                data.data.replies
            }
        }
    ).flow
}