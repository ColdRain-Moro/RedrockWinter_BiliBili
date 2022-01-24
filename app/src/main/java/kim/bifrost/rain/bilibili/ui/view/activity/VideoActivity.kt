package kim.bifrost.rain.bilibili.ui.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.widget.MediaController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.ActivityVideoBinding
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoPlayData
import kim.bifrost.rain.bilibili.ui.view.adapter.StandardVPAdapter
import kim.bifrost.rain.bilibili.ui.view.fragment.VideoCommentFragment
import kim.bifrost.rain.bilibili.ui.view.fragment.VideoIntroduceFragment
import kim.bifrost.rain.bilibili.ui.viewmodel.VideoViewModel
import kim.bifrost.rain.bilibili.utils.toastConcurrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class VideoActivity : BaseVMActivity<VideoViewModel, ActivityVideoBinding>(
    isCancelStatusBar = false
) {

    private val simpleVideoInfo by lazy { App.gson.fromJson(intent.getStringExtra("data"), SimpleVideoInfo::class.java) }
    private val client = OkHttpClient()

    private lateinit var videoInfo: VideoInfo
    private lateinit var videoPlayData: VideoPlayData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK
        // 异步加载
        lifecycleScope.launch(Dispatchers.IO) {
            videoInfo = viewModel.getVideoInfo()
            withContext(Dispatchers.Main) {
                binding.vp2Video.adapter = StandardVPAdapter(supportFragmentManager, lifecycle, listOf("简介", "评论")) { d, i ->
                    when (i) {
                        0 -> VideoIntroduceFragment.newInstance(videoInfo)
                        1 -> VideoCommentFragment.newInstance(videoInfo)
                        else -> error("error state")
                    }
                }
                TabLayoutMediator(binding.tlVideo, binding.vp2Video) { tab, i ->
                    when (i) {
                        0 -> tab.text = "简介"
                        1 -> tab.text = "评论"
                    }
                }.attach()
            }
            videoPlayData = viewModel.getVideoPlayData(cid = videoInfo.data.cid)
            Drawable.createFromStream(URL(simpleVideoInfo.pic).openStream(), "image.jpg").apply {
                withContext(Dispatchers.Main) {
                    binding.vv.background = this@apply
                }
                // 缓存视频
                val videoTemp = File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), "${videoInfo.data.bvid}.flv")
                if (!videoTemp.exists()) {
                    // 没有缓存过 缓存
                    toastConcurrent("缓存视频中...")
                    videoTemp.createNewFile()
                    FileOutputStream(videoTemp).use { fos ->
                        client.newCall(
                            Request.Builder()
                                .url(videoPlayData.data.durl[0].url)
                                .addHeader("referer", "https://www.bilibili.com/video/${videoInfo.data.bvid}")
                                .build()
                        ).execute().body!!.byteStream().use {
                            fos.write(it.readBytes())
                        }
                    }
                    toastConcurrent("缓存完成")
                }
                withContext(Dispatchers.Main) {
                    binding.vv.background = null
                    binding.vv.setVideoPath(videoTemp.path)
                }
            }
        }
        binding.vv.apply {
            // 添加播放控制条
            setMediaController(MediaController(this@VideoActivity))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override val viewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VideoViewModel(simpleVideoInfo) as T
            }
        }

    companion object {
        fun start(context: Context, data: SimpleVideoInfo) {
            val starter = Intent(context, VideoActivity::class.java)
                .putExtra("data", App.gson.toJson(data))
            context.startActivity(starter)
        }

        fun start(context: Context, data: SimpleVideoInfo, bundle: Bundle) {
            val starter = Intent(context, VideoActivity::class.java)
                .putExtra("data", App.gson.toJson(data))
            context.startActivity(starter, bundle)
        }
    }
}