package com.example.assisment.room

import androidx.room.Entity

@Entity(tableName = "Video")
data class Entity(
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val videoLink: String
)