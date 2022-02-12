package kim.bifrost.rain.bilibili.ui.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.R
import kim.bifrost.rain.bilibili.databinding.ActivityVideoBinding
import kim.bifrost.rain.bilibili.model.database.AppDatabase
import kim.bifrost.rain.bilibili.model.database.toDBBean
import kim.bifrost.rain.bilibili.model.web.ApiService
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoPlayData
import kim.bifrost.rain.bilibili.ui.view.adapter.StandardVPAdapter
import kim.bifrost.rain.bilibili.ui.view.fragment.VideoCommentFragment
import kim.bifrost.rain.bilibili.ui.view.fragment.VideoIntroduceFragment
import kim.bifrost.rain.bilibili.ui.view.fragment.dialog.VideoSettingDialogFragment
import kim.bifrost.rain.bilibili.ui.viewmodel.VideoViewModel
import kim.bifrost.rain.bilibili.widget.MyDanmakuView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.doikki.videocontroller.StandardVideoController
import java.io.File
import java.io.FileOutputStream

class VideoActivity : BaseVMActivity<VideoViewModel, ActivityVideoBinding>(
    isCancelStatusBar = false
) {

    private val simpleVideoInfo by lazy { App.gson.fromJson(intent.getStringExtra("data"), SimpleVideoInfo::class.java) }

    private val danmakuView by lazy { MyDanmakuView(this) }

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
            // 缓存弹幕
            val danmakuTemp = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "tempDanmaku_${videoInfo.bvid}.xml")
            if (danmakuTemp.exists()) {
                danmakuTemp.delete()
            }
            danmakuTemp.createNewFile()
            FileOutputStream(danmakuTemp).use { fos ->
                fos.write(ApiService.getXmlDanmaku(videoInfo.cid))
            }
            val xmlDanmakuInput = danmakuTemp.inputStream()
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
                danmakuView.createParser(xmlDanmakuInput)
            }
            videoPlayData = viewModel.getVideoPlayData(cid = videoInfo.cid, bvid = videoInfo.bvid)
                withContext(Dispatchers.Main) {
                    binding.vv.apply {
                        setUrl(videoPlayData.data.durl[0].url, hashMapOf("referer" to "https://www.bilibili.com/video/${videoInfo.bvid}"
                            , "User-Agent" to "Mozilla/5.0 BiliDroid/5.37.0 (bbcallen@gmail.com)"))
                        setVideoController(
                            StandardVideoController(this@VideoActivity).also {
                                it.addDefaultControlComponent(videoInfo.title, false)
                                it.addControlComponent(danmakuView)
                            }
                        )
                        start()
                    }
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
        // 退出DialogFragment的回调
        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                if (App.enableDanmaku) {
                    danmakuView.show()
                } else {
                    danmakuView.hide()
                }
            }
        }, false)
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
            R.id.video_settings -> {
                VideoSettingDialogFragment().show(supportFragmentManager, "巴拉巴拉")
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.video_toolbar, menu)
        return true
    }

    override fun onPause() {
        super.onPause()
        binding.vv.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.vv.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vv.release()
    }


    override fun onBackPressed() {
        if (!binding.vv.onBackPressed()) {
            super.onBackPressed()
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