package kim.bifrost.rain.bilibili.ui.view.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.Surface
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.ActivityVideoBinding
import kim.bifrost.rain.bilibili.model.database.AppDatabase
import kim.bifrost.rain.bilibili.model.database.toDBBean
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoPlayData
import kim.bifrost.rain.bilibili.ui.view.adapter.StandardVPAdapter
import kim.bifrost.rain.bilibili.ui.view.fragment.VideoCommentFragment
import kim.bifrost.rain.bilibili.ui.view.fragment.VideoIntroduceFragment
import kim.bifrost.rain.bilibili.ui.viewmodel.VideoViewModel
import kim.bifrost.rain.bilibili.utils.toast
import kim.bifrost.rain.bilibili.utils.toastConcurrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class VideoActivity : BaseVMActivity<VideoViewModel, ActivityVideoBinding>(
    isCancelStatusBar = false
) {

    private val simpleVideoInfo by lazy { App.gson.fromJson(intent.getStringExtra("data"), SimpleVideoInfo::class.java) }
    private val client = OkHttpClient()

    private lateinit var normalLayoutParams : ViewGroup.LayoutParams

    private lateinit var videoInfo: VideoInfo.Data
    private lateinit var videoPlayData: VideoPlayData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK
        // Toolbar
        binding.toolbar.apply {
            setSupportActionBar(this)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.title = ""
            }
        }
        // 异步加载
        lifecycleScope.launch(Dispatchers.IO) {
            videoInfo = if (intent.getStringExtra("data2") == null) viewModel.getVideoInfo()
                else App.gson.fromJson(intent.getStringExtra("data2"), VideoInfo.Data::class.java)
            withContext(Dispatchers.Main) {
                binding.vp2Video.adapter = StandardVPAdapter(supportFragmentManager, lifecycle, listOf("1", "2")) { _, i ->
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
                binding.vv.setOnPreparedListener { toast("prepare") }
            }
            videoPlayData = viewModel.getVideoPlayData(cid = videoInfo.cid, bvid = videoInfo.bvid)
                // 缓存视频
                val videoTemp = File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), "${videoInfo.bvid}.flv")
                if (!videoTemp.exists()) {
                    // 没有缓存过 缓存
                    toastConcurrent("缓存视频中...")
                    videoTemp.createNewFile()
                    FileOutputStream(videoTemp).use { fos ->
                        client.newCall(
                            Request.Builder()
                                .url(videoPlayData.data.durl[0].url)
                                .addHeader("referer", "https://www.bilibili.com/video/${videoInfo.bvid}")
                                .build()
                        ).execute().body!!.byteStream().use {
                            fos.write(it.readBytes())
                        }
                    }
                    toastConcurrent("缓存完成")
                }
                withContext(Dispatchers.Main) {
                    binding.vv.setVideoPath(videoTemp.path)
                }
            // 观看历史相关逻辑
            val dao = AppDatabase.impl.getHistoryWatchDao()
            if (dao.selectByBvid(videoInfo.bvid).isEmpty()) {
                // 没有观看过，新增
                dao.insert(videoInfo.toDBBean())
            } else {
                // 观看过，更新上次观看时间
                dao.update(videoInfo.bvid, System.currentTimeMillis())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val rot = windowManager.defaultDisplay.rotation
        if (rot == Surface.ROTATION_90 || rot == Surface.ROTATION_270) {
            normalLayoutParams = binding.rlVv.layoutParams
            val layoutParams = CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, CollapsingToolbarLayout.LayoutParams.MATCH_PARENT)
            binding.rlVv.layoutParams = layoutParams
        } else if (rot == Surface.ROTATION_0) {
            binding.rlVv.layoutParams = normalLayoutParams
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

        /**
         * 传入序列化为json的VideoInfo或VideoRecommends.Data
         * 由于实际结构一致，它们可以被统一反序列化为VideoInfo
         *
         * @param context
         * @param dataStr
         */
        fun start(context: Context, dataStr: String) {
            val starter = Intent(context, VideoActivity::class.java)
                .putExtra("data2", dataStr)
            context.startActivity(starter)
        }
    }
}