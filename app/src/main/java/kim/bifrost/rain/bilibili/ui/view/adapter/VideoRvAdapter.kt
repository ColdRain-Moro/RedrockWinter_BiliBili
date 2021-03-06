package kim.bifrost.rain.bilibili.ui.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.ItemVideoBinding
import kim.bifrost.rain.bilibili.databinding.ItemVideoIntroduceHeaderBinding
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
class VideoRvAdapter(
    private val context: Context,
    private val data: List<VideoRecommends.Data>,
    private val initHeaderCallBack: ItemVideoIntroduceHeaderBinding.() -> Unit
) : RecyclerView.Adapter<VideoRvAdapter.Holder>() {
    inner class Holder(val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (getItemViewType(bindingAdapterPosition) == 1) {
                    val obj = data[bindingAdapterPosition - 1]
                    VideoActivity.start(context, App.gson.toJson(obj))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return if (viewType == 1) Holder(
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) else Holder(
            ItemVideoIntroduceHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (getItemViewType(position) == 1) {
            val obj = data[position - 1]
            (holder.binding as ItemVideoBinding).apply {
                Glide.with(context)
                    .load(obj.pic)
                    .centerCrop()
                    .into(holder.binding.img)
                tvAuthor.text = obj.owner.name
                tvTitle.text = obj.title
                tvInfo.text = "\uD83D\uDCF9 ${obj.stat.view.toNumberFormattedString()}   \uD83D\uDCAC ${obj.stat.reply.toNumberFormattedString()}"
            }
        } else {
            // header
            initHeaderCallBack(holder.binding as ItemVideoIntroduceHeaderBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return 0
        return 1
    }

    override fun getItemCount(): Int = data.size + 1
}