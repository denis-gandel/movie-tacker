package com.example.movietracker.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("isFavorite"), Index("isWatched")])
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val year: Int,
    val imageUrl: String,
    var isWatched: Boolean = false,
    var isFavorite: Boolean = false
)
