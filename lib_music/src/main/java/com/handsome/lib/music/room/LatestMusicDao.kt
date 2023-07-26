package com.handsome.lib.music.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.handsome.lib.music.model.WrapPlayInfo
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver

//最近播放音乐

@Dao
interface LatestMusicDao {

    @Insert
    fun insertMusic(wrapPlayInfo: WrapPlayInfo) : Single<Long>

    //查询最近播放的所有音乐
    //第一个参数表示起始索引，第二个参数表示加载数量
    @Query("select * from WrapPlayInfo limit :offset,:limit")
    suspend fun loadAllMusic(offset : Int,limit : Int) : List<WrapPlayInfo>

    /**
     * 删除WrapPlayInfo表格里的所有记录
     * @return Int 返回删除的条数
     */
    @Query("DELETE FROM WrapPlayInfo")
    suspend fun deleteTableAndRun(): Int

}