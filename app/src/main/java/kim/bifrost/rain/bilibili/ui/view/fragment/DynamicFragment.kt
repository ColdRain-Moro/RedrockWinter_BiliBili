package kim.bifrost.rain.bilibili.ui.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.databinding.FragmentDynamicBinding
import kim.bifrost.rain.bilibili.ui.view.adapter.DynamicRvAdapter
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.DynamicFragViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * kim.bifrost.rain.bilibili.ui.view.fragment.DynamicFragment
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/19 14:04
 **/
class DynamicFragment : BaseVMFragment<DynamicFragViewModel, FragmentDynamicBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val data = viewModel.getRegions()
            withContext(Dispatchers.Main) {
                binding.rvDynamic.apply {
                    layoutManager = GridLayoutManager(requireContext(), 4)
                    adapter = DynamicRvAdapter(data, requireContext())
                }
            }
        }
    }
}