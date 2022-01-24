package kim.bifrost.rain.bilibili.ui.view.fragment

import android.os.Bundle
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.FragmentVideoCommentBinding
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.VideoCommentFragViewModel

/**
 * kim.bifrost.rain.bilibili.ui.view.fragment.VideoCommentFragment
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/23 11:01
 **/
class VideoCommentFragment : BaseVMFragment<VideoCommentFragViewModel, FragmentVideoCommentBinding>() {

    companion object {
        fun newInstance(data: VideoInfo): VideoCommentFragment {
            val args = Bundle()
            args.putString("data", App.gson.toJson(data))
            val fragment = VideoCommentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}