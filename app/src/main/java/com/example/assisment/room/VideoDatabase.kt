package com.example.assisment.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entity::class],version = 1,exportSchema = false)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao() : VideoDao

}