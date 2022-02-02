package kim.bifrost.rain.bilibili.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kim.bifrost.rain.bilibili.model.web.bean.VideoInfo

/**
 * kim.bifrost.rain.bilibili.model.database.Bean
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/2 0:22
 **/
@Entity(tableName = "watch_history")
data class WatchHistoryVideoBean(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int? = null,
    @ColumnInfo(name = "bvid", typeAffinity = ColumnInfo.TEXT)
    val bvid: String,
    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    val title: String,
    @ColumnInfo(name = "author", typeAffinity = ColumnInfo.TEXT)
    val author: String,
    @ColumnInfo(name = "play", typeAffinity = ColumnInfo.INTEGER)
    val play: Int,
    @ColumnInfo(name = "comment", typeAffinity = ColumnInfo.INTEGER)
    val comment: Int,
    @ColumnInfo(name = "img", typeAffinity = ColumnInfo.TEXT)
    val img: String,
    @ColumnInfo(name = "last_watch", typeAffinity = ColumnInfo.INTEGER)
    val lastWatch: Long
) {
    fun toSimpleVideoInfo() {

    }
}

fun VideoInfo.Data.toDBBean(lastWatch: Long = System.currentTimeMillis()) = WatchHistoryVideoBean(
    bvid = bvid,
    title = title,
    author = owner.name,
    play = stat.view,
    comment = stat.reply,
    img = pic,
    lastWatch = lastWatch
)