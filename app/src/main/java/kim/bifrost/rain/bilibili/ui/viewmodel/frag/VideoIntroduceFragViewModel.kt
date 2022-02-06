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
    suspend fun thumpsUp(aid: Int, like: Int) = ApiService.like(aid = aid, like = like)
    suspend fun hasThumpsUp(aid: Int) = ApiService.isLike(aid = aid).data == 1
    suspend fun coin(aid: Int, multiply: Int) = ApiService.coin(aid = aid, multiply = multiply)
    suspend fun hasCoinAmount(aid: Int) = ApiService.isCoin(aid = aid).data.multiply
    suspend fun allLike(aid: Int) = ApiService.allLike(aid = aid)
    suspend fun hasCollected(aid: Int) = ApiService.isCollect(aid = aid).data.favoured
    suspend fun getUserInfo(mid: Int) = ApiService.getUserCard(mid, false).data
}