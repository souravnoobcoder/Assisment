package com.example.assisment.room

import android.app.Application
import androidx.lifecycle.LiveData

class Repo(application: Application?) {
    private val dao: VideoDao
    private val videoList: LiveData<List<Entity>>
    suspend fun insert(videos: List<Entity>) = dao.insert(videos)

    suspend fun deleteAll()=dao.deleteAll()

    fun getVideoLists() = videoList

    init {
        val database = VideoDatabase.databaseInstance(application?.applicationContext!!)
        dao= database?.videoDao()!!
        videoList=dao.getVideosList()
    }

}