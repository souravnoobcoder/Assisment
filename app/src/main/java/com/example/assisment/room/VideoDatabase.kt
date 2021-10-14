package com.example.assisment.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assisment.App

@Database(entities = [Entity::class],version = 1,exportSchema = false)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao() : VideoDao

    companion object{
        private var database : VideoDatabase? = null
        fun databaseInstance(context: Context) : VideoDatabase? {
            if (database==null){
                database= Room.databaseBuilder(
                    context.applicationContext,
                    VideoDatabase::class.java,
                    "VideosDatabase"
                ).build()
            }
            return database
        }
    }


}