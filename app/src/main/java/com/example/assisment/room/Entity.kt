package com.example.assisment.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Video")
data class Entity(
    var thumbnailUrl: String,
    var title: String,
    var videoLink: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}