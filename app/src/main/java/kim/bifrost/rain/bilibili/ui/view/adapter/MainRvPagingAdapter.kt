package kim.bifrost.rain.bilibili.ui.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.base.BaseItemCallBack
import kim.bifrost.coldrain.wanandroid.base.BasePagingAdapter
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.ItemBannerBinding
import kim.bifrost.rain.bilibili.databinding.ItemRecommendBinding
import kim.bifrost.rain.bilibili.model.web.bean.HomePage
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.ui.view.activity.VideoActivity
import kim.bifrost.rain.bilibili.utils.toBvid

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.MainRvPagingAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/20 15:56
 **/
class MainRvPagingAdapter(private val activity: Activity, private val onBannerInit: ItemBannerBinding.() -> Unit)
    : BasePagingAdapter<ViewBinding, HomePage.Data.Item>(
    activity,
    BaseItemCallBack { a, b -> a.param == b.param }
) {

    override fun getDataBinding(parent: ViewGroup, viewType: Int): ViewBinding {
        if (viewType == 0) return ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: Holder<ViewBinding>, position: Int) {
        val type = getItemViewType(position)
        if (type == 1) {
            (holder.binding as ItemRecommendBinding).apply {
                val data = getItem(position)!!
                title.text = data.title
                author.text = data.args.upName
                Glide.with(context)
                    .load(data.cover)
                    .centerCrop()
                    .into(videoSnapshot)
            }
        } else {
            (holder.binding as ItemBannerBinding).apply {
                onBannerInit(this)
            }
        }
    }

    override val holderInit: Holder<ViewBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                if (getItemViewType(absoluteAdapterPosition) == 1) {
                    val data = getItem(absoluteAdapterPosition)!!
                    val json = "{ \"bvid\": ${parseBVidFromParam(data.param, data.goto)} }"
                    VideoActivity.start(
                        context,
                        App.gson.fromJson(json, SimpleVideoInfo::class.java),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create((binding as ItemRecommendBinding).videoSnapshot, "vv")).toBundle()!!
                    )
                }
            }
        }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    private fun parseBVidFromParam(param: String, goto: String): String {
        return when (goto) {
            "av" -> param.toLong().toBvid()
            "bv" -> param
            else -> error("Not Implement goto: $goto")
        }
    }
}