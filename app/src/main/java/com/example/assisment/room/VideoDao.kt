package com.example.assisment.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Insert
    suspend fun insert(video: List<Entity>)

    @Query("DELETE FROM Video")
    suspend fun deleteAll()

    @Query("SELECT * FROM Video")
    fun getVideosList(): Flow<List<Entity>>
}