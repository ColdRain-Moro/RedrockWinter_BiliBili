package kim.bifrost.rain.bilibili.ui.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.text.buildSpannedString
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.R
import kim.bifrost.rain.bilibili.databinding.ActivityMainBinding
import kim.bifrost.rain.bilibili.model.UserManager
import kim.bifrost.rain.bilibili.model.web.bean.LoginUserData
import kim.bifrost.rain.bilibili.model.web.bean.QrCodeRequestBean
import kim.bifrost.rain.bilibili.ui.view.adapter.StandardVPAdapter
import kim.bifrost.rain.bilibili.ui.view.fragment.DynamicFragment
import kim.bifrost.rain.bilibili.ui.view.fragment.HomePageFragment
import kim.bifrost.rain.bilibili.ui.viewmodel.MainViewModel
import kim.bifrost.rain.bilibili.utils.dynamicDrawableSpan
import kim.bifrost.rain.bilibili.utils.toast
import kim.bifrost.rain.bilibili.utils.toastConcurrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MainActivity : BaseVMActivity<MainViewModel, ActivityMainBinding>(
    isCancelStatusBar = false
) {

    private lateinit var requestData: QrCodeRequestBean.Data

    // 启动登录页面
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                // 不需要实际的结果，只需要在这个activity退出之后执行
                lifecycleScope.launch(Dispatchers.IO) {
                    val data = viewModel.responseQRCode(requestData.auth_code)
                    when (data.code) {
                        0 -> {
                            toastConcurrent("登录成功!")
                            App.setToken(data.data?.access_token,
                                data.data?.refresh_token,
                                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(data.data!!.expires_in.toLong()))
                            flushLoginState()
                        }
                        else -> {
                            toastConcurrent(data.message)
                        }
                    }
                }
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vp2Main.apply {
            adapter = StandardVPAdapter(
                supportFragmentManager,
                lifecycle,
                listOf("首页", "动态")
            ) { _, i ->
                when (i) {
                    0 -> HomePageFragment()
                    1 -> DynamicFragment()
                    else -> error("wrong status")
                }
            }
            // 滑动页面时选中对应的BottomNav选项
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavMain.menu.getItem(position).isChecked = true
                }
            })
            // 禁止用户滑动，防止与Fragment内的ViewPager产生滑动冲突
            isUserInputEnabled = false
        }
        // 侧滑栏
        binding.navView.apply {
            setNavigationItemSelectedListener {
                when (it.itemId) {
                    // 首页
                    R.id.nav_home -> {
                        binding.vp2Main.currentItem = 0
                    }
                    // 播放历史
                    R.id.nav_history -> {
                        startActivity(Intent(this@MainActivity, WatchHistoryActivity::class.java))
                    }
                    // 离线缓存
                    R.id.nav_temp_video -> {
                        // TODO
                    }
                    // 收藏
                    R.id.nav_my_collect -> needLogin {
                        // TODO
                    }
                    // 稍后再看
                    R.id.nav_next_watch -> needLogin {
                        // TODO
                    }
                    R.id.nav_logout -> {
                        if (UserManager.isLogged) {
                            UserManager.logout()
                            flushLoginState()
                            toast("登出成功")
                        }
                    }
                }
                binding.drawerLayout.closeDrawers()
                true
            }
        }
        // 顶部用户头像
        binding.userIcon.apply {
            // 点击打开侧滑栏
            setOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        // 底部导航
        binding.bottomNavMain.apply {
            labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED
            setOnItemSelectedListener {
                if (binding.vp2Main.scrollState != ViewPager2.SCROLL_STATE_IDLE) return@setOnItemSelectedListener false
                when (it.itemId) {
                    R.id.bottom_nav_home -> binding.vp2Main.currentItem = 0
                    R.id.bottom_nav_dynamic -> binding.vp2Main.currentItem = 1
                }
                true
            }
        }
        // 搜索
        binding.searchView.apply {
            isSubmitButtonEnabled = true
            isQueryRefinementEnabled = true
            setIconifiedByDefault(false)
            //隐藏icon
            findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
                .apply {
                    setImageDrawable(null)
                    visibility = View.GONE
                }
            queryHint = "发现更多精彩"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isEmpty()) return false
                    SearchResultActivity.start(this@MainActivity, query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean = true
            })
        }
        // 头像点击事件
        binding.navView.getHeaderView(0).apply {
            findViewById<ShapeableImageView>(R.id.iv_user).apply {
                setOnClickListener {
                    if (!UserManager.isLogged) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            requestLogin()
                        }
                    }
                }
            }
        }
        flushLoginState()
    }

    // 请求登录
    private suspend fun requestLogin() {
        viewModel.requestQRCode().data?.let {
            requestData = it
            startForResult.launch(
                Intent(this, WebPageActivity::class.java)
                    .putExtra("title", "登录")
                    .putExtra("url", it.url)
            )
        }
    }

    // 需要登录才能执行的操作
    private fun needLogin(func: () -> Unit) {
        if (UserManager.isLogged) {
            func()
        } else {
            lifecycleScope.launch(Dispatchers.IO) { requestLogin() }
        }
    }

    // 刷新登录状态
    @SuppressLint("SetTextI18n")
    private fun flushLoginState() {
        // 若已经登录开协程请求登录数据
        if (UserManager.isLogged) {
            lifecycleScope.launch(Dispatchers.IO) {
                val data = viewModel.getLoginData()
                withContext(Dispatchers.Main) {
                    when (data.code) {
                        0 -> {
                            // 加载头像
                            Glide.with(binding.userIcon)
                                .load(data.data.face)
                                .into(binding.userIcon)
                            binding.navView.getHeaderView(0).apply {
                                findViewById<ShapeableImageView>(R.id.iv_user).apply {
                                    Glide.with(this)
                                        .load(data.data.face)
                                        .into(this)
                                }
                                findViewById<ImageView>(R.id.iv_drawer_background).apply {
                                    Glide.with(this)
                                        .load(R.drawable.bili_drawerbg_logined)
                                        .into(this)
                                }
                                lifecycleScope.launch(Dispatchers.IO) {
                                    val span = buildSpannedString {
                                        append(data.data.name)
                                    }
                                    withContext(Dispatchers.Main) {
                                        findViewById<TextView>(R.id.tv_user_name).text = span
                                    }
                                }
                                findViewById<TextView>(R.id.tv_user_info).text =
                                    "硬币: ${data.data.coins}"
                            }
                        }
                        else -> {
                            toast("请求个人信息错误: ${data.message}")
                        }
                    }
                }
            }
        } else {
            Glide.with(binding.userIcon)
                .load(R.drawable.noface)
                .into(binding.userIcon)
            binding.navView.getHeaderView(0).apply {
                findViewById<ShapeableImageView>(R.id.iv_user).apply {
                    Glide.with(this)
                        .load(R.drawable.noface)
                        .into(this)
                }
                findViewById<ImageView>(R.id.iv_drawer_background).apply {
                    Glide.with(this)
                        .load(R.drawable.bili_drawerbg_logined)
                        .into(this)
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    val span = buildSpannedString {
                        append("未登录")
                    }
                    withContext(Dispatchers.Main) {
                        findViewById<TextView>(R.id.tv_user_name).text = span
                    }
                }
                findViewById<TextView>(R.id.tv_user_info).text =
                    "登录体验更多功能"
            }
        }
    }
}