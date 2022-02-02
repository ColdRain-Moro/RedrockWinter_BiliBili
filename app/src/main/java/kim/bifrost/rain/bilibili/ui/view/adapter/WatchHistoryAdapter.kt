package kim.bifrost.rain.bilibili.ui.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.databinding.ItemVideoBinding
import kim.bifrost.rain.bilibili.model.database.WatchHistoryVideoBean
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.ui.view.activity.VideoActivity
import kim.bifrost.rain.bilibili.utils.castToType
import kim.bifrost.rain.bilibili.utils.toNumberFormattedString

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.WatchHistoryAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/2 13:23
 **/
class WatchHistoryAdapter(
    private val context: Context,
    private val items: List<WatchHistoryVideoBean>,
    ) :  RecyclerView.Adapter<WatchHistoryAdapter.Holder>() {
    inner class Holder(val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val item = items[bindingAdapterPosition]
                VideoActivity.start(context, mapOf("bvid" to item.bvid).castToType<SimpleVideoInfo>())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            Glide.with(context)
                .load(item.img)
                .into(img)
            tvAuthor.text = item.author
            tvInfo.text = "\uD83D\uDCF9 ${item.play.toNumberFormattedString()}   \uD83D\uDCAC ${item.comment.toNumberFormattedString()}"
            tvTitle.text = item.title
        }
    }

    override fun getItemCount(): Int = items.size
}