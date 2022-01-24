package kim.bifrost.rain.bilibili.ui.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.ItemVideoBinding
import kim.bifrost.rain.bilibili.model.web.bean.VideoRecommends
import kim.bifrost.rain.bilibili.ui.view.activity.VideoActivity
import kim.bifrost.rain.bilibili.utils.toNumberFormattedString

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.VideoRvAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/24 12:28
 **/
class VideoRvAdapter(private val context: Context, private val data: List<VideoRecommends.Data>) : RecyclerView.Adapter<VideoRvAdapter.Holder>() {
    inner class Holder(val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val obj = data[bindingAdapterPosition]
                VideoActivity.start(context, App.gson.toJson(obj))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val obj = data[position]
        holder.binding.apply {
            Glide.with(context)
                .load(obj.pic)
                .centerCrop()
                .into(holder.binding.img)
            tvAuthor.text = obj.owner.name
            tvTitle.text = obj.title
            tvInfo.text = "${obj.stat.view.toNumberFormattedString()}   ${obj.stat.reply.toNumberFormattedString()}"
        }
    }

    override fun getItemCount(): Int = data.size
}