package kim.bifrost.rain.bilibili.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.databinding.ItemDynamicBinding
import kim.bifrost.rain.bilibili.model.web.bean.RegionResponse
import kim.bifrost.rain.bilibili.ui.view.activity.RegionActivity

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.DynamicRvAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/5 18:53
 **/
class DynamicRvAdapter(private val data: RegionResponse, private val context: Context) : RecyclerView.Adapter<DynamicRvAdapter.Holder>() {
    inner class Holder(val binding: ItemDynamicBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val data = data.data[bindingAdapterPosition]
                RegionActivity.start(context, data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemDynamicBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = data.data[position]
        holder.binding.apply {
            tv.text = data.name
            Glide.with(icon)
                .load(data.logo)
                .into(icon)
        }
    }

    override fun getItemCount(): Int = data.data.size

}