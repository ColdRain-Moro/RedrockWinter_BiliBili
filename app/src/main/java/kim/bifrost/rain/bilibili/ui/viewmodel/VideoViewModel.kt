package kim.bifrost.rain.bilibili.ui.viewmodel

import androidx.lifecycle.ViewModel
import kim.bifrost.rain.bilibili.model.web.ApiService
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.VideoViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/21 13:13
 **/
class VideoViewModel(private val data: SimpleVideoInfo) : ViewModel() {
    suspend fun getVideoPlayData(cid: Int) = ApiService.getVideo(avid = null, bvid = data.bvid, cid = cid)
    suspend fun getVideoInfo() = ApiService.getVideoInfo(null, data.bvid)
}