package kim.bifrost.rain.bilibili.ui.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.databinding.ActivityMainBinding
import kim.bifrost.rain.bilibili.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : BaseVMActivity<MainViewModel, ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}