package com.example.assisment.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class VideoViewModel(application: Application) :AndroidViewModel(application){
    private val repository : Repo=Repo(application)
    val videoList: LiveData<List<Entity>> =repository.getVideoLists()

    suspend fun insert(videos: List<Entity>)=repository.insert(videos)

    suspend fun deleteAll()=repository.deleteAll()
}