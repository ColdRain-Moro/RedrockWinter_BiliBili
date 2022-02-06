package kim.bifrost.rain.bilibili.ui.viewmodel.frag

import androidx.lifecycle.ViewModel
import kim.bifrost.rain.bilibili.model.web.ApiService
import kim.bifrost.rain.bilibili.model.web.bean.RegionResponse

/**
 * kim.bifrost.rain.bilibili.ui.viewmodel.frag.DynamicFragViewModel
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/19 14:04
 **/
class DynamicFragViewModel : ViewModel() {
    suspend fun getRegions(): RegionResponse = ApiService.getRegions()
}