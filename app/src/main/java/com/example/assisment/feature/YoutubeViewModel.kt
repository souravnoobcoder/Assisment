package com.example.assisment.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    private val repository: YoutubeRepository
): ViewModel() {
    fun getVideos(nextPageToken: String) =
        repository.getVideos(nextPageToken).asLiveData()
}