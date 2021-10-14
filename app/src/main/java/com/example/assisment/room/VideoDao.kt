package com.example.assisment.room

import androidx.lifecycle.LiveData
import androidx.room.Dao

@Dao
interface VideoDao {

    suspend fun insert(vararg videos: Entity)

    suspend fun deleteAll()

    fun getVideosList(): LiveData<List<Entity>>
}