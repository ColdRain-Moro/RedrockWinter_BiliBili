package kim.bifrost.rain.bilibili.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * kim.bifrost.rain.bilibili.model.database.HistoryWatchDao
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/2 0:21
 **/
@Dao
interface HistoryWatchDao {
    /**
     * 查询所有数据
     */
    @Query("SELECT * FROM watch_history")
    fun queryAll(): Flow<List<WatchHistoryVideoBean>>

    /**
     * 插入一条数据
     */
    @Insert
    fun insert(entity: WatchHistoryVideoBean)

    /**
     * 通过bvid选取一条数据
     *
     * @param bvid
     * @return
     */
    @Query("SELECT * FROM watch_history WHERE bvid=:bvid")
    fun selectByBvid(bvid: String): List<WatchHistoryVideoBean>

    /**
     * 搜索
     *
     * @param query 关键词
     * @return
     */
    @Query("SELECT * FROM watch_history WHERE title LIKE '%'+:query+'%'")
    fun search(query: String): Flow<List<WatchHistoryVideoBean>>

    /**
     * 更新视频最后观看日期
     *
     * @param bvid
     * @param lastWatch
     */
    @Query("UPDATE watch_history SET last_watch=:lastWatch WHERE bvid=:bvid")
    fun update(bvid: String, lastWatch: Long)

    /**
     * 清空表中所有内容
     */
    @Query("DELETE FROM watch_history")
    fun delete()
}