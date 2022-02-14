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

    // 不支持的分区
    private val regionUnsupported = arrayOf("直播", "广告", "放映厅", "小视频", "专栏", "音频", "相簿", "会员购", "游戏中心")

    // 筛掉不支持的分区，防止点击闪退
    private val datas by lazy { data.data.filterNot { regionUnsupported.contains(it.name) } }

    inner class Holder(val binding: ItemDynamicBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val data = datas[bindingAdapterPosition]
                RegionActivity.start(context, data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemDynamicBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = datas[position]
        holder.binding.apply {
            tv.text = data.name
            Glide.with(icon)
                .load(data.logo)
                .into(icon)
        }
    }

    override fun getItemCount(): Int = datas.size

}