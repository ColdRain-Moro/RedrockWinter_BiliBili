package kim.bifrost.rain.bilibili.ui.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.FragmentVideoCommentBinding
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.ui.view.adapter.ReplyPagingAdapter
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.VideoCommentFragViewModel
import kim.bifrost.rain.bilibili.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * kim.bifrost.rain.bilibili.ui.view.fragment.VideoCommentFragment
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/23 11:01
 **/
class VideoCommentFragment : BaseVMFragment<VideoCommentFragViewModel, FragmentVideoCommentBinding>() {

    private val videoInfo by lazy { App.gson.fromJson(requireArguments().getString("data"), VideoInfo.Data::class.java) }

    @Suppress("UNCHECKED_CAST")
    override val viewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VideoCommentFragViewModel(videoInfo.aid) as T
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val replyAdapter = ReplyPagingAdapter(requireContext())
        binding.rvReply.apply {
            adapter = replyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL), Color.LTGRAY)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.replies.collectLatest {
                replyAdapter.submitData(it)
            }
        }
        replyAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.srlReply.isRefreshing = true
                }
                is LoadState.NotLoading -> {
                    binding.srlReply.isRefreshing = false
                }
                is LoadState.Error -> {
                    (it.refresh as LoadState.Error).error.printStackTrace()
                    toast("错误: ${(it.refresh as LoadState.Error).error.message}")
                }
            }
        }
        binding.srlReply.setOnRefreshListener {
            replyAdapter.refresh()
        }
    }

    companion object {
        fun newInstance(data: VideoInfo.Data): VideoCommentFragment {
            val args = Bundle()
            args.putString("data", App.gson.toJson(data))
            val fragment = VideoCommentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}