package kim.bifrost.rain.bilibili.ui.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.FragmentRegionChildrenBinding
import kim.bifrost.rain.bilibili.model.web.ApiService
import kim.bifrost.rain.bilibili.model.web.bean.RegionResponse
import kim.bifrost.rain.bilibili.ui.view.adapter.ChildrenRegionRVAdapter
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.ChildrenRegionFragViewModel
import kim.bifrost.rain.bilibili.utils.startVideoActivityByBvid
import kim.bifrost.rain.bilibili.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * kim.bifrost.rain.bilibili.ui.view.fragment.ChildrenRegionFragment
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/5 20:19
 **/
class ChildrenRegionFragment : BaseVMFragment<ChildrenRegionFragViewModel, FragmentRegionChildrenBinding>() {

    private val data by lazy { App.gson.fromJson(requireArguments().getString("data"), RegionResponse.Data.Children::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val adapter = ChildrenRegionRVAdapter(requireContext()) {
                lifecycleScope.launch {
                    startVideoActivityByBvid(requireContext(), it.bvid)
                }
            }
            withContext(Dispatchers.Main) {
                binding.rvRegionChildren.apply {
                    layoutManager = GridLayoutManager(requireContext(), 2)
                    this.adapter = adapter
                }
                binding.slRegionChildren.setOnRefreshListener {
                    adapter.refresh()
                }
                adapter.addLoadStateListener {
                    when (it.refresh) {
                        is LoadState.Loading -> {
                            binding.slRegionChildren.isRefreshing = true
                        }
                        is LoadState.NotLoading -> {
                            binding.slRegionChildren.isRefreshing = false
                        }
                        is LoadState.Error -> {
                            toast("发生错误: ${(it.refresh as LoadState.Error).error.message}")
                        }
                    }
                }
            }
            viewModel.getDataFlow(data.tid).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        fun newInstance(data: RegionResponse.Data.Children): ChildrenRegionFragment {
            val args = Bundle()
            args.putString("data", App.gson.toJson(data))
            val fragment = ChildrenRegionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}