package kim.bifrost.rain.bilibili.ui.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.base.BaseItemCallBack
import kim.bifrost.coldrain.wanandroid.base.BasePagingAdapter
import kim.bifrost.rain.bilibili.databinding.ItemRecommendBinding
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.ui.view.activity.VideoActivity

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.MainRvPagingAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/20 15:56
 **/
class MainRvPagingAdapter(private val activity: Activity)
    : BasePagingAdapter<ItemRecommendBinding, SimpleVideoInfo>(
    activity,
    BaseItemCallBack { a, b -> a.aid == b.aid }
) {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemRecommendBinding {
        return ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: Holder<ItemRecommendBinding>, position: Int) {
        holder.binding.apply {
            val data = getItem(position)!!
            title.text = data.title
            author.text = data.author
            Glide.with(context)
                .load(data.pic)
                .centerCrop()
                .into(videoSnapshot)
        }
    }

    override val holderInit: Holder<ItemRecommendBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                VideoActivity.start(
                    context,
                    getItem(absoluteAdapterPosition)!!,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create(binding.videoSnapshot, "vv")).toBundle()!!
                )
            }
        }
}