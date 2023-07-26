package com.handsome.lib.music.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.handsome.lib.music.model.WrapPlayInfo
import com.handsome.lib.util.BaseApp

//创建数据库，第一个表是最近播放的音乐
@Database(version = 1, entities = [WrapPlayInfo::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun wrapPlayInfoDao() : LatestMusicDao

    companion object{

        private var instance : AppDatabase? = null

        @Synchronized
        fun getDataBase() : AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                BaseApp.mContext,
                AppDatabase::class.java,
                "app_database"
            ).build().apply { instance = this }
        }
    }
}