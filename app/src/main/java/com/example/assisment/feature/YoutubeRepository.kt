package com.example.assisment.feature

import androidx.room.withTransaction
import com.example.assisment.api.YoutubeApi
import com.example.assisment.room.VideoDao
import com.example.assisment.room.VideoDatabase
import com.example.assisment.util.getVideoList
import com.example.assisment.util.networkBoundResource
import javax.inject.Inject

class YoutubeRepository @Inject constructor(
    private val api: YoutubeApi,
    private val database: VideoDatabase
) {
    private val dao: VideoDao = database.videoDao()
    fun getVideos(nextPageToken: String) = networkBoundResource(
        query = {
            dao.getVideosList()
        },
        fetch = {
            when {
                nextPageToken.isEmpty() -> api.getList()
                else -> api.getNextList(nextPageToken)
            }
        },
        saveFetchResult = {
            when {
                nextPageToken.isEmpty() -> database.withTransaction {
                    dao.deleteAll()
                    dao.insert(getVideoList(it.items))
                }
                else -> dao.insert(getVideoList(it.items))
            }

        }
    )
}