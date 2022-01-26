package kim.bifrost.rain.bilibili.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kim.bifrost.rain.bilibili.databinding.ItemSearchSuggestionBinding

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.SearchSuggestionAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/26 14:55
 **/
class SearchSuggestionAdapter(private val suggestions: List<String>, private val clickCallback: (String) -> Unit) : RecyclerView.Adapter<SearchSuggestionAdapter.Holder>() {
    inner class Holder(val binding: ItemSearchSuggestionBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val item = suggestions[bindingAdapterPosition]
                clickCallback(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemSearchSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.tv.text = suggestions[position]
    }

    override fun getItemCount(): Int = suggestions.size
}