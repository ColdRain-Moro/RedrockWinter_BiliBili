package kim.bifrost.rain.bilibili.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kim.bifrost.rain.bilibili.databinding.ItemBannerVpBinding
import kim.bifrost.rain.bilibili.ui.view.activity.WebPageActivity

/**
 * kim.bifrost.rain.bilibili.ui.view.adapter.BannerAdapter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/15 20:53
 **/
class BannerAdapter(private val context: Context) : RecyclerView.Adapter<BannerAdapter.Holder>() {

    val items = arrayOf(
        BannerData(
            "绝地反击！谷爱凌再摘一银！",
            "https://i0.hdslb.com/bfs/feed-admin/1aeba37c04ee1ac44aa63b2d251d8e445abbd017.png@976w_550h_1c.webp",
            "https://www.bilibili.com/blackboard/activity-648bGqYW3W.html?spm_id_from=333.1007.0.0"
        ),
        BannerData(
            "必剪上线解说音色，快来激情演说！",
            "https://i0.hdslb.com/bfs/feed-admin/d598ed8e4101005635e3dced33f2c656ef50af76.jpg@976w_550h_1c.webp",
            "https://www.bilibili.com/blackboard/activity-6XkN4Dltml.html?spm_id_from=333.1007.0.0"
        ),
        BannerData(
            "情人节听情歌！快来听听！",
            "https://i0.hdslb.com/bfs/feed-admin/5eb166933f0f1eb91dcfdb8c4355011c7636666c.jpg@976w_550h_1c.webp",
            "https://www.bilibili.com/blackboard/activity-foKiAkNBDb.html?spm_id_from=333.1007.0.0"
        )
    )

    private var itemClickListener: (Int) -> Unit = {
        WebPageActivity.startActivity(context, items[it].url, items[it].title)
    }

    inner class Holder(val binding: ItemBannerVpBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener(absoluteAdapterPosition % items.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemBannerVpBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = items[position % items.size]
        // 使用Glide加载网络图片
        Glide.with(holder.itemView)
            .load(data.imagePath)
            .centerCrop()
            .into(holder.binding.homeVpImg)
        holder.binding.homeVpText.text = data.title
    }

    override fun getItemCount(): Int = if (items.isEmpty()) 0 else Integer.MAX_VALUE
}

data class BannerData(
    val title: String,
    val imagePath: String,
    val url: String
)