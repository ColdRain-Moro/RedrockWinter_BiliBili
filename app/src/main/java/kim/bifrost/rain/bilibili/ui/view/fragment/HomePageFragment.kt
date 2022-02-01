package kim.bifrost.rain.bilibili.ui.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.FragmentHomePageBinding
import kim.bifrost.rain.bilibili.ui.view.activity.SearchResultActivity
import kim.bifrost.rain.bilibili.ui.view.adapter.MainRvPagingAdapter
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.HomePageFragViewModel
import kim.bifrost.rain.bilibili.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * kim.bifrost.rain.bilibili.ui.view.fragment.HomePageFragment
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/19 14:03
 **/
class HomePageFragment : BaseVMFragment<HomePageFragViewModel, FragmentHomePageBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MainRvPagingAdapter(requireActivity())
        binding.rvHome.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.recommendList.collectLatest {
                adapter.submitData(it)
            }
        }
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.srlMain.isRefreshing = true
                }
                is LoadState.NotLoading -> {
                    binding.srlMain.isRefreshing = false
                }
                is LoadState.Error -> {
                    (it.refresh as LoadState.Error).error.printStackTrace()
                    toast("错误: ${(it.refresh as LoadState.Error).error.message}")
                }
            }
        }
        binding.srlMain.setOnRefreshListener {
            adapter.refresh()
        }
    }
}