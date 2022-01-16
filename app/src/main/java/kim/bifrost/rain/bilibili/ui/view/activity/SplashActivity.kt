package kim.bifrost.rain.bilibili.ui.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kim.bifrost.coldrain.wanandroid.base.BaseActivity
import kim.bifrost.rain.bilibili.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(TimeUnit.SECONDS.toMillis(1))
            // 显示图像
            binding.pic.visibility = View.VISIBLE
            delay(TimeUnit.SECONDS.toMillis(1))
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            // 出栈
            finish()
        }
    }
}