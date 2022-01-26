package kim.bifrost.rain.bilibili.ui.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kim.bifrost.coldrain.wanandroid.base.BaseVMActivity
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.ActivitySearchResultBinding
import kim.bifrost.rain.bilibili.ui.view.adapter.SearchPagingAdapter
import kim.bifrost.rain.bilibili.ui.view.adapter.SearchSuggestionAdapter
import kim.bifrost.rain.bilibili.ui.viewmodel.SearchResultViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchResultActivity : BaseVMActivity<SearchResultViewModel, ActivitySearchResultBinding>(
    isCancelStatusBar = false
) {

    private lateinit var keyword: String

    private val clickSuggestionCallback: (String) -> Unit = {
        binding.svSearchResult.setQuery(it, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = intent.getStringExtra("keyword")!!
        binding.toolbarSearchResult.apply {
            setSupportActionBar(this)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.title = ""
            }
        }
        // adapter 也是可变的，随关键词的变化而变化
        var adapter: RecyclerView.Adapter<*> = SearchPagingAdapter(this)
        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(this@SearchResultActivity)
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(this@SearchResultActivity, DividerItemDecoration.VERTICAL), Color.LTGRAY)
        }
        // job是可变的，每搜索一次不同的关键词都会取消并覆盖最新的引用
        var job = lifecycleScope.launch(Dispatchers.IO) {
            viewModel.search(keyword).collectLatest {
                (adapter as SearchPagingAdapter).submitData(it)
            }
        }
        binding.svSearchResult.apply {
            isSubmitButtonEnabled = true
            isQueryRefinementEnabled = true
            setIconifiedByDefault(false)
            //隐藏icon
            findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
                .apply {
                    setImageDrawable(null)
                    visibility = View.GONE
                }
            setQuery(keyword, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isEmpty()) return false
                    job.cancel()
                    // 覆写adapter,重新布局
                    adapter = SearchPagingAdapter(this@SearchResultActivity)
                    binding.rvSearchResult.adapter = adapter
                    job = lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.search(keyword).collectLatest {
                            (adapter as SearchPagingAdapter).submitData(it)
                        }
                    }
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    job.cancel()
                    lifecycleScope.launch(Dispatchers.IO) {
                        val keywords = if (newText.isEmpty()) viewModel.hotkeys() else viewModel.suggestions(newText)
                        withContext(Dispatchers.Main) {
                            adapter = SearchSuggestionAdapter(keywords, clickSuggestionCallback)
                            binding.rvSearchResult.adapter = adapter
                        }
                    }
                    return true
                }
            })
        }
    }

    companion object {
        fun start(context: Context, keyword: String) {
            val starter = Intent(context, SearchResultActivity::class.java)
                .putExtra("keyword", keyword)
            context.startActivity(starter)
        }
    }
}