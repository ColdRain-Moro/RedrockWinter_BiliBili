package kim.bifrost.rain.bilibili.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.coldrain.wanandroid.base.BasePagingAdapter
import kim.bifrost.rain.bilibili.base.BaseItemCallBack
import kim.bifrost.rain.bilibili.databinding.ItemRecommendBinding
import kim.bifrost.rain.bilibili.model.web.bean.RegionContentResponse

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.ChildrenRegionRVAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/5 21:18
 **/
class ChildrenRegionRVAdapter(context: Context) : BasePagingAdapter<ItemRecommendBinding, RegionContentResponse.Data.New>(
    context,
    BaseItemCallBack { a, b ->
        a.param == b.param
    }
) {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemRecommendBinding
        = ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun onBindViewHolder(holder: Holder<ItemRecommendBinding>, position: Int) {
        val data = getItem(position)!!
        holder.binding.apply {
            author.text = data.name
            title.text = data.title
            Glide.with(context)
                .load(data.cover)
                .into(videoSnapshot)
        }
    }

    override val holderInit: Holder<ItemRecommendBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {

            }
        }
}