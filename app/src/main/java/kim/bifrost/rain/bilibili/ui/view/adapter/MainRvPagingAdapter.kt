package kim.bifrost.rain.bilibili.ui.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.base.BaseItemCallBack
import kim.bifrost.coldrain.wanandroid.base.BasePagingAdapter
import kim.bifrost.rain.bilibili.App
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
class MainRvPagingAdapter(private val activity: Activity)
    : BasePagingAdapter<ItemRecommendBinding, HomePage.Data.Item>(
    activity,
    BaseItemCallBack { a, b -> a.param == b.param }
) {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemRecommendBinding {
        return ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: Holder<ItemRecommendBinding>, position: Int) {
        holder.binding.apply {
            val data = getItem(position)!!
            title.text = data.title
            author.text = data.args.upName
            Glide.with(context)
                .load(data.cover)
                .centerCrop()
                .into(videoSnapshot)
        }
    }

    override val holderInit: Holder<ItemRecommendBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                val data = getItem(absoluteAdapterPosition)!!
                val json = "{ \"bvid\": ${parseBVidFromParam(data.param, data.goto)} }"
                VideoActivity.start(
                    context,
                    App.gson.fromJson(json, SimpleVideoInfo::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create(binding.videoSnapshot, "vv")).toBundle()!!
                )
            }
        }

    private fun parseBVidFromParam(param: String, goto: String): String {
        return when (goto) {
            "av" -> param.toLong().toBvid()
            "bv" -> param
            else -> error("Not Implement goto: $goto")
        }
    }
}