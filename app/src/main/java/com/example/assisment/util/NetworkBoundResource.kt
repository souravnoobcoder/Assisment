package com.example.assisment.util

import com.example.assisment.api.Item
import com.example.assisment.api.YoutubeApi
import com.example.assisment.room.Entity
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()
    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Resource.Error(throwable, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }
    emitAll(flow)
}

fun getVideoList(videos: List<Item>?): List<Entity>{
    val videosList: MutableList<Entity> = ArrayList()
    for (i in 0 until YoutubeApi.firstCallSize) {
        val item = videos?.get(i)
        val entity = Entity(
            thumbnailUrl = item?.snippet?.thumbnails?.medium?.url!!,
            title = item.snippet.title,
            videoLink = item.id
        )
        videosList.add(entity)
    }
    return videosList.toList()
}