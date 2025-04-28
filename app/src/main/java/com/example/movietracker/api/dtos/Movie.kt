package com.example.movietracker.api.dtos

typealias Movies = ArrayList<Movie>

data class Movie (
    val id: Int,
    val title: String,
    val year: Int,
    val imageUrl: String
)
