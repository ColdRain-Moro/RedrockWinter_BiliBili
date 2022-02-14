package kim.bifrost.rain.bilibili.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.coldrain.wanandroid.base.BasePagingAdapter
import kim.bifrost.rain.bilibili.base.BaseItemCallBack
import kim.bifrost.rain.bilibili.databinding.ItemRecommendBinding
import kim.bifrost.rain.bilibili.model.web.bean.PagerListBean
import kim.bifrost.rain.bilibili.model.web.bean.RegionContentResponse
import kim.bifrost.rain.bilibili.utils.startVideoActivityByBvid

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.ChildrenRegionRVAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/5 21:18
 **/
class ChildrenRegionRVAdapter(context: Context, private val onClick: (PagerListBean.Data.Archive) -> Unit) : BasePagingAdapter<ItemRecommendBinding, PagerListBean.Data.Archive>(
    context,
    BaseItemCallBack { a, b ->
        a.aid == b.aid
    }
) {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemRecommendBinding
        = ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun onBindViewHolder(holder: Holder<ItemRecommendBinding>, position: Int) {
        val data = getItem(position)!!
        holder.binding.apply {
            author.text = data.owner.name
            title.text = data.title
            Glide.with(context)
                .load(data.pic)
                .into(videoSnapshot)
        }
    }

    override val holderInit: Holder<ItemRecommendBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                onClick(getItem(bindingAdapterPosition)!!)
            }
        }
}