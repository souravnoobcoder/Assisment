package com.example.assisment.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assisment.App

@Database(entities = [Entity::class],version = 1,exportSchema = false)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao() : VideoDao
    private val database : VideoDatabase? = null
    val databaseInstance : VideoDatabase?
        get(){
            if (database==null){
                Room.databaseBuilder(
                    App.getAppInstance()?.applicationContext!!,
                    VideoDatabase::class.java,
                    "VideosDatabase"
                )
            }
            return database
        }

}