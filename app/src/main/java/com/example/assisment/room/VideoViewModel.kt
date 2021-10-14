package com.example.assisment.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class VideoViewModel(application: Application) :AndroidViewModel(application){
    private val repository: Repo = Repo(application)
    val videoList: LiveData<List<Entity>> = repository.getVideoLists()

    suspend fun insert(video: Entity) = repository.insert(video)

    suspend fun deleteAll() = repository.deleteAll()
}