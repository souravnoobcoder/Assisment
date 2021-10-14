package com.example.assisment

data class Default(
    val height: Int,
    val url: String,
    val width: Int
)

data class High(
    val height: Int,
    val url: String,
    val width: Int
)

data class Trending(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo
)

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet
)

data class Localized(
    val description: String,
    val title: String
)

data class Maxres(
    val height: Int,
    val url: String,
    val width: Int
)

data class Medium(
    val height: Int,
    val url: String,
    val width: Int
)

data class PageInfo(
    val resultsPerPage: Int,
    val totalResults: Int
)

data class Snippet(
    val categoryId: String,
    val channelId: String,
    val channelTitle: String,
    val defaultAudioLanguage: String,
    val defaultLanguage: String,
    val description: String,
    val liveBroadcastContent: String,
    val localized: Localized,
    val publishedAt: String,
    val tags: List<String>,
    val thumbnails: Thumbnails,
    val title: String
)

data class Standard(
    val height: Int,
    val url: String,
    val width: Int
)

data class Thumbnails(
    val default: Default,
    val high: High,
    val maxres: Maxres,
    val medium: Medium,
    val standard: Standard
)