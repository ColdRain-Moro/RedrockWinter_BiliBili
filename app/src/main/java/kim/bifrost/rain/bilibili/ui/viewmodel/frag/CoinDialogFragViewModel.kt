package kim.bifrost.rain.bilibili.ui.viewmodel.frag

import androidx.lifecycle.ViewModel
import kim.bifrost.rain.bilibili.model.web.ApiService

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.frag.CoinDialogFragViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/3 14:28
 **/
class CoinDialogFragViewModel : ViewModel() {
    suspend fun coin(aid: Int, multiply: Int) = ApiService.coin(aid = aid, multiply = multiply)
}