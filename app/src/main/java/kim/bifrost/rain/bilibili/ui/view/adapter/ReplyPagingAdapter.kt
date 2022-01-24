package kim.bifrost.rain.bilibili.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.base.BaseItemCallBack
import kim.bifrost.coldrain.wanandroid.base.BasePagingAdapter
import kim.bifrost.rain.bilibili.databinding.ItemVideoFeedbackBinding
import kim.bifrost.rain.bilibili.model.web.bean.ReplyBean
import kim.bifrost.rain.bilibili.utils.toNumberFormattedString

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.ReplyRvAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/24 23:47
 **/
class ReplyPagingAdapter(context: Context) : BasePagingAdapter<ItemVideoFeedbackBinding, ReplyBean.Data.Reply>(
    context,
    BaseItemCallBack { a, b -> a.rpid == b.rpid }
) {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemVideoFeedbackBinding = ItemVideoFeedbackBinding.inflate(
        LayoutInflater.from(parent.context), parent, false)

    override fun onBindViewHolder(holder: Holder<ItemVideoFeedbackBinding>, position: Int) {
        val data = getItem(position)!!
        holder.binding.apply {
            tvAuthor.text = data.member.uname
            tvContent.text = data.content.message
            tvThumpsUp.text = data.like.toNumberFormattedString()
            tvDate.text = data.reply_control.time_desc
            Glide.with(context)
                .load(data.member.avatar)
                .into(ivAuthor)
        }
    }

}