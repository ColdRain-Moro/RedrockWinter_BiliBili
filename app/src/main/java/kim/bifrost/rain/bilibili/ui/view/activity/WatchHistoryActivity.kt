package kim.bifrost.rain.bilibili.ui.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.R
import kim.bifrost.rain.bilibili.databinding.ActivitySearchResultBinding
import kim.bifrost.rain.bilibili.ui.view.adapter.WatchHistoryAdapter
import kim.bifrost.rain.bilibili.ui.viewmodel.WatchHistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchHistoryActivity : BaseVMActivity<WatchHistoryViewModel, ActivitySearchResultBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolbarSearchResult.apply {
            setSupportActionBar(this)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.title = ""
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.remove_all -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.removeAll()
                        }
                    }
                }
                true
            }
        }
        binding.rvSearchResult.layoutManager = LinearLayoutManager(this)
        // adapter 也是可变的，随关键词的变化而变化
        lifecycleScope.launch(Dispatchers.IO) {
            // 订阅数据库变更，及时更新
            viewModel.getAllHistory().collectLatest {
                withContext(Dispatchers.Main) {
                    binding.rvSearchResult.adapter = WatchHistoryAdapter(this@WatchHistoryActivity, it)
                }
            }
        }
        // 搜索条
        binding.svSearchResult.apply {
            isQueryRefinementEnabled = true
            setIconifiedByDefault(false)
            //隐藏icon
            findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
                .apply {
                    setImageDrawable(null)
                    visibility = View.GONE
                }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean = false

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val data = viewModel.getAllHistory().first()
                            withContext(Dispatchers.Main) {
                                binding.rvSearchResult.adapter = WatchHistoryAdapter(this@WatchHistoryActivity, data)
                            }
                        }
                        return true
                    }
                    lifecycleScope.launch(Dispatchers.IO) {
                        val data = viewModel.search(newText).first()
                        withContext(Dispatchers.Main) {
                            binding.rvSearchResult.adapter = WatchHistoryAdapter(this@WatchHistoryActivity, data)
                        }
                    }
                    return true
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.watch_history_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}