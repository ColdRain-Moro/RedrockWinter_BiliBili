package kim.bifrost.rain.bilibili.ui.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.FragmentHomePageBinding
import kim.bifrost.rain.bilibili.ui.view.activity.SearchResultActivity
import kim.bifrost.rain.bilibili.ui.view.adapter.BannerAdapter
import kim.bifrost.rain.bilibili.ui.view.adapter.MainRvPagingAdapter
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.HomePageFragViewModel
import kim.bifrost.rain.bilibili.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        // 轮播图回调
        val adapter = MainRvPagingAdapter(requireActivity()) {
            val vpLooper = object : Runnable {
                override fun run() {
                    // 控制滑动速度
                    lifecycleScope.launch {
                        if (vpBanner.scrollState == ViewPager2.SCROLL_STATE_IDLE) {
                            vpBanner.beginFakeDrag()
                            for (i in 1..40) {
                                delay(15)
                                vpBanner.fakeDragBy(-vpBanner.width.toFloat() / 40)
                            }
                            vpBanner.endFakeDrag()
                        }
                    }
                    vpBanner.postDelayed(this, 5000)
                }
            }
            vpBanner.apply {
                adapter = BannerAdapter(requireContext())
                if (!isFakeDragging) {
                    currentItem = 4000
                }
                postDelayed(vpLooper, 5000)
            }
        }
        binding.rvHome.apply {
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int
                        = when (position) {
                            0 -> 2
                            else -> 1
                        }
                }
            }
            this.adapter = adapter
        }
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.homePageFlow.collectLatest {
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