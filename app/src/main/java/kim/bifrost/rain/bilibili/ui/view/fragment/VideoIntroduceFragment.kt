package kim.bifrost.rain.bilibili.ui.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kim.bifrost.coldrain.wanandroid.base.BaseVMFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.R
import kim.bifrost.rain.bilibili.databinding.FragmentVideoIntroduceBinding
import kim.bifrost.rain.bilibili.model.UserManager
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.ui.view.adapter.VideoRvAdapter
import kim.bifrost.rain.bilibili.ui.view.fragment.dialog.CoinDialogFragment
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.VideoIntroduceFragViewModel
import kim.bifrost.rain.bilibili.utils.toNumberFormattedString
import kim.bifrost.rain.bilibili.utils.toast
import kim.bifrost.rain.bilibili.utils.toastConcurrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                val authorInfo = viewModel.getUserInfo(data.owner.mid)
                val tags = viewModel.getTags(data.aid)
                withContext(Dispatchers.Main) {
                    rvVideosRecommend.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = VideoRvAdapter(requireContext(), list) {
                            Glide.with(requireContext())
                                .load(data.owner.face)
                                .into(userIcon)
                            up.text = data.owner.name
                            // 获取作者信息
                            info.text = "${authorInfo.card.fans.toNumberFormattedString()}粉丝  ${authorInfo.archive_count.toNumberFormattedString()}视频"
                            title.text = data.title
                            videoInfo.text = "\uD83D\uDCF9 ${data.stat.view.toNumberFormattedString()}      \uD83D\uDCAC ${data.stat.reply.toNumberFormattedString()}    ${simpleDateFormat.format(Date(data.pubdate * 1000))}       ${data.bvid}     未经作者授权禁止转载"
                            simpleIntroduce.text = data.desc
                            tvCoin.text = data.stat.coin.toNumberFormattedString()
                            tvCollect.text = data.stat.favorite.toNumberFormattedString()
                            tvShare.text = data.stat.share.toNumberFormattedString()
                            tvThumbsUp.text = data.stat.like.toNumberFormattedString()
                            btnThumbsUp.setOnClickListener {
                                // 点赞
                                requireLogin {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        val hasThumpsUp = viewModel.hasThumpsUp(data.aid)
                                        val data = viewModel.thumpsUp(data.aid, if (hasThumpsUp) 1 else 0)
                                        toastConcurrent(data.data.toast)
                                        withContext(Dispatchers.Main) {
                                            thumpsUpAnimate(btnThumbsUp, !hasThumpsUp)
                                        }
                                    }
                                }
                            }
                            // 投币
                            btnCoin.setOnClickListener {
                                requireLogin {
                                    // 打开投币的DialogFragment
                                    CoinDialogFragment.newInstance(data.aid)
                                        .show(parentFragmentManager, "投币")
                                }
                            }
                            // 三连
                            btnThumbsUp.setOnLongClickListener {
                                requireLogin {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        val data = viewModel.allLike(data.aid)
                                        // 成功就播放动画
                                        withContext(Dispatchers.Main) {
                                            if (data.data.like) {
                                                thumpsUpAnimate(btnThumbsUp, true)
                                            }
                                            if (data.data.coin) {
                                                coinAnimate(btnCoin)
                                            }
                                            if (data.data.fav) {
                                                coinAnimate(btnCollect)
                                            }
                                            if (data.message != "0") {
                                                toast(data.message)
                                            }
                                        }
                                    }
                                }
                                true
                            }
                            if (UserManager.isLogged) {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    if (viewModel.hasThumpsUp(data.aid)) {
                                        withContext(Dispatchers.Main) {
                                            btnThumbsUp.setColorFilter(0xF06292)
                                        }
                                    }
                                    if (viewModel.hasCoinAmount(data.aid) > 0) {
                                        withContext(Dispatchers.Main) {
                                            btnCoin.setColorFilter(0xF06292)
                                        }
                                    }
                                    if (viewModel.hasCollected(data.aid)) {
                                        withContext(Dispatchers.Main) {
                                            btnCollect.setColorFilter(0xF06292)
                                        }
                                    }
                                }
                            }
                            if (tags.code == 0) {
                                tags.data.forEach { tag ->
                                    tlTag.addTag(requireContext(), tag.tag_name, Color.GRAY)
                                }
                            }
                        }
                        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL), Color.LTGRAY)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 刷新状态
        if (UserManager.isLogged) {
            refreshState()
        }
    }

    private fun refreshState() {
        val header = binding.rvVideosRecommend.layoutManager?.findViewByPosition(0)
        if (header != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                if (viewModel.hasThumpsUp(data.aid)) {
                    withContext(Dispatchers.Main) {
                        header.findViewById<ImageView>(R.id.btn_thumbs_up).setColorFilter(0xF06292)
                    }
                }
                if (viewModel.hasCoinAmount(data.aid) > 0) {
                    withContext(Dispatchers.Main) {
                        header.findViewById<ImageView>(R.id.btn_coin).setColorFilter(0xF06292)
                    }
                }
                if (viewModel.hasCollected(data.aid)) {
                    withContext(Dispatchers.Main) {
                        header.findViewById<ImageView>(R.id.btn_collect).setColorFilter(0xF06292)
                    }
                }
            }
        }
    }

    private fun requireLogin(func: () -> Unit) {
        if (UserManager.isLogged) {
            func()
        } else {
            toast("未登录。请先登录")
        }
    }

    private fun coinAnimate(iv: ImageView) {
        ObjectAnimator.ofInt(iv, "colorFilter", 0xF06292).apply {
            duration = 300
            start()
        }
        AnimatorSet().apply {
            // 缩放
            play(ObjectAnimator.ofFloat(iv, "scaleX", 1.0f).apply {
                duration = 200
            }).after(ObjectAnimator.ofFloat(iv, "scaleX", 1.2f).apply {
                duration = 200
            })
            play(ObjectAnimator.ofFloat(iv, "scaleY", 1.0f).apply {
                duration = 200
            }).after(ObjectAnimator.ofFloat(iv, "scaleY", 1.2f).apply {
                duration = 200
            })
            start()
        }
    }

    private fun thumpsUpAnimate(iv: ImageView, thumpsUp: Boolean) {
        if (thumpsUp) {
            // 改变颜色
            ObjectAnimator.ofInt(iv, "colorFilter", 0xF06292).apply {
                duration = 300
                start()
            }
            AnimatorSet().apply {
                // 缩放
                play(ObjectAnimator.ofFloat(iv, "scaleX", 1.0f).apply {
                    duration = 200
                }).after(ObjectAnimator.ofFloat(iv, "scaleX", 1.2f).apply {
                    duration = 200
                })
                play(ObjectAnimator.ofFloat(iv, "scaleY", 1.0f).apply {
                    duration = 200
                }).after(ObjectAnimator.ofFloat(iv, "scaleY", 1.2f).apply {
                    duration = 200
                })
                start()
            }
        } else {
            iv.clearColorFilter()
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