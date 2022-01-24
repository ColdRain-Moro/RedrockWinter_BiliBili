package kim.bifrost.rain.bilibili.ui.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.FragmentVideoIntroduceBinding
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.VideoIntroduceFragViewModel
import kim.bifrost.rain.bilibili.utils.toNumberFormattedString
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

    private val data by lazy { App.gson.fromJson(requireArguments().getString("data"), VideoInfo::class.java) }
    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            Glide.with(requireContext())
                .load(data.data.owner.face)
                .into(userIcon)
            up.text = data.data.owner.name
            // 获取作者信息
            Log.d("Test", "(VideoIntroduceFragment.kt:40) ==> ${data.data.pubdate}")
            title.text = data.data.title
            videoInfo.text = "${data.data.stat.view.toNumberFormattedString()}      ${data.data.stat.reply.toNumberFormattedString()}    ${simpleDateFormat.format(Date(data.data.pubdate * 1000))}       ${data.data.bvid}     未经作者授权禁止转载"
            simpleIntroduce.text = data.data.desc
            tvCoin.text = data.data.stat.coin.toNumberFormattedString()
            tvCollect.text = data.data.stat.favorite.toNumberFormattedString()
            tvShare.text = data.data.stat.share.toNumberFormattedString()
            tvThumbsUp.text = data.data.stat.like.toNumberFormattedString()
        }
    }

    companion object {
        fun newInstance(data: VideoInfo): VideoIntroduceFragment {
            val args = Bundle()
            args.putString("data", App.gson.toJson(data))
            val fragment = VideoIntroduceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}