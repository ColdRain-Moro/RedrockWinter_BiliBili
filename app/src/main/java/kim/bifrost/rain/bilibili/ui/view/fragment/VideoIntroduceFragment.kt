package kim.bifrost.rain.bilibili.ui.view.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.FragmentVideoIntroduceBinding
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.ui.view.adapter.VideoRvAdapter
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.VideoIntroduceFragViewModel
import kim.bifrost.rain.bilibili.utils.toNumberFormattedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.format
import java.text.SimpleDateFormat
import java.util.*

/**
 * kim.bifrost.rain.bilibili.ui.view.fragment.FragmentVideoIntroduce
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/23 0:23
 **/
class VideoIntroduceFragment : BaseVMFragment<VideoIntroduceFragViewModel, FragmentVideoIntroduceBinding>() {

    private val data by lazy { App.gson.fromJson(requireArguments().getString("data"), VideoInfo.Data::class.java) }
    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleScope.launch(Dispatchers.IO) {
                val list = viewModel.getVideoRecommends(data.bvid).data
                withContext(Dispatchers.Main) {
                    rvVideosRecommend.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = VideoRvAdapter(requireContext(), list) {
                            Glide.with(requireContext())
                                .load(data.owner.face)
                                .into(userIcon)
                            up.text = data.owner.name
                            // 获取作者信息
                            title.text = data.title
                            videoInfo.text = "\uD83D\uDCF9 ${data.stat.view.toNumberFormattedString()}      \uD83D\uDCAC ${data.stat.reply.toNumberFormattedString()}    ${simpleDateFormat.format(Date(data.pubdate * 1000))}       ${data.bvid}     未经作者授权禁止转载"
                            simpleIntroduce.text = data.desc
                            tvCoin.text = data.stat.coin.toNumberFormattedString()
                            tvCollect.text = data.stat.favorite.toNumberFormattedString()
                            tvShare.text = data.stat.share.toNumberFormattedString()
                            tvThumbsUp.text = data.stat.like.toNumberFormattedString()
                        }
                        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL), Color.LTGRAY)
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(data: VideoInfo.Data): VideoIntroduceFragment {
            val args = Bundle()
            args.putString("data", App.gson.toJson(data))
            val fragment = VideoIntroduceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}