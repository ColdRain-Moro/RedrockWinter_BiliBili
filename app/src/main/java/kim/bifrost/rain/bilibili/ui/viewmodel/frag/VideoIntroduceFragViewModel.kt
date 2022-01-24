package kim.bifrost.rain.bilibili.ui.viewmodel.frag

import androidx.lifecycle.ViewModel
import kim.bifrost.rain.bilibili.model.web.ApiService

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.frag.VideoIntroduceFragViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/23 0:25
 **/
class VideoIntroduceFragViewModel : ViewModel() {
    suspend fun getVideoRecommends(bvid: String) = ApiService.getVideoRecommends(bvid = bvid)
}