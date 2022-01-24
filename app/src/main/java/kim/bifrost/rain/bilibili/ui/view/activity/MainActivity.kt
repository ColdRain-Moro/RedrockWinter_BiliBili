package kim.bifrost.rain.bilibili.ui.view.activity

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.R
import kim.bifrost.rain.bilibili.databinding.ActivityMainBinding
import kim.bifrost.rain.bilibili.ui.view.adapter.StandardVPAdapter
import kim.bifrost.rain.bilibili.ui.view.fragment.DynamicFragment
import kim.bifrost.rain.bilibili.ui.view.fragment.HomePageFragment
import kim.bifrost.rain.bilibili.ui.view.fragment.MyStatusFragment
import kim.bifrost.rain.bilibili.ui.viewmodel.MainViewModel

class MainActivity : BaseVMActivity<MainViewModel, ActivityMainBinding>(
    isCancelStatusBar = false
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(R.color.primaryColor)
        binding.vp2Main.apply {
            adapter = StandardVPAdapter(
                supportFragmentManager,
                lifecycle,
                listOf("首页", "动态", "我的")
            ) { _, i ->
                when (i) {
                    0 -> HomePageFragment()
                    1 -> DynamicFragment()
                    2 -> MyStatusFragment()
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
        binding.bottomNavMain.apply {
            labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED
            setOnItemSelectedListener {
                if (binding.vp2Main.scrollState != ViewPager2.SCROLL_STATE_IDLE) return@setOnItemSelectedListener false
                when (it.itemId) {
                    R.id.bottom_nav_home -> binding.vp2Main.currentItem = 0
                    R.id.bottom_nav_dynamic -> binding.vp2Main.currentItem = 1
                    R.id.bottom_my_status -> binding.vp2Main.currentItem = 2
                }
                true
            }
        }
    }
}