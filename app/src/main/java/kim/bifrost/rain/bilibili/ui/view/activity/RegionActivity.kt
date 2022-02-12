package kim.bifrost.rain.bilibili.ui.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.ActivityRegionBinding
import kim.bifrost.rain.bilibili.model.web.bean.RegionResponse
import kim.bifrost.rain.bilibili.ui.view.adapter.StandardVPAdapter
import kim.bifrost.rain.bilibili.ui.view.fragment.ChildrenRegionFragment
import kim.bifrost.rain.bilibili.ui.viewmodel.RegionViewModel

class RegionActivity : BaseVMActivity<RegionViewModel, ActivityRegionBinding>(
    isCancelStatusBar = false
) {
    private val data by lazy { App.gson.fromJson(intent.getStringExtra("data"), RegionResponse.Data::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Toolbar
        binding.toolbar.apply {
            setSupportActionBar(this)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.title = data.name
            }
        }
        binding.vp2Region.adapter = StandardVPAdapter(supportFragmentManager, lifecycle, data.children) { list, i ->
            ChildrenRegionFragment.newInstance(list[i])
        }
        TabLayoutMediator(binding.tlRegion, binding.vp2Region) { tab, i ->
            tab.text = data.children[i].name
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    companion object {
        fun start(context: Context, data: RegionResponse.Data) {
            val starter = Intent(context, RegionActivity::class.java)
                .putExtra("data", App.gson.toJson(data))
            context.startActivity(starter)
        }
    }
}