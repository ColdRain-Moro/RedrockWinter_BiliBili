package kim.bifrost.rain.bilibili.ui.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.coldrain.wanandroid.base.BasePagingAdapter
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.base.BaseItemCallBack
import kim.bifrost.rain.bilibili.databinding.ItemVideoBinding
import kim.bifrost.rain.bilibili.model.web.bean.SimpleVideoInfo
import kim.bifrost.rain.bilibili.model.web.bean.VideoSearchResultBean
import kim.bifrost.rain.bilibili.ui.view.activity.VideoActivity
import kim.bifrost.rain.bilibili.utils.htmlDecode
import kim.bifrost.rain.bilibili.utils.toNumberFormattedString

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.SearchPagingAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/26 14:24
 **/
class SearchPagingAdapter(context: Context) : BasePagingAdapter<ItemVideoBinding, VideoSearchResultBean.Data.Result>(
    context,
    BaseItemCallBack { a, b ->
        a.aid == b.aid
    }
) {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemVideoBinding =
        ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder<ItemVideoBinding>, position: Int) {
        val obj = getItem(position)!!
        holder.binding.apply {
            Glide.with(context)
                .load("https:" + obj.pic)
                .centerCrop()
                .into(holder.binding.img)
            tvAuthor.text = obj.author.htmlDecode()
            tvTitle.text = obj.title.htmlDecode()
            tvInfo.text = "\uD83D\uDCF9 ${obj.play.toNumberFormattedString()}   \uD83D\uDCAC ${obj.video_review.toNumberFormattedString()}"
        }
    }

    override val holderInit: Holder<ItemVideoBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                // SimpleVideoInfo在VideoActivity中需要用到的字段和Result是重合的
                // 故直接用gson暴力转型，由于gson走的unsafe，所以为空的非空字段能跳过kt的空安全编译检查
                val bean = App.gson.fromJson(App.gson.toJson(getItem(bindingAdapterPosition)), SimpleVideoInfo::class.java)
                VideoActivity.start(context, bean)
            }
        }
}