package com.example.assisment.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VideoDao {
    @Insert
    suspend fun insert(video: Entity)

    @Query("DELETE FROM Video")
    suspend fun deleteAll()

    @Query("SELECT * FROM Video")
    fun getVideosList(): LiveData<List<Entity>>
}