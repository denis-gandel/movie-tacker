package com.example.movietracker.api.repositories

import com.example.movietracker.api.dtos.Movie
import com.example.movietracker.api.dtos.Movies
import com.example.movietracker.api.services.MovieApiService
import com.example.movietracker.db.dao.MovieDao
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApiService: MovieApiService
) {
    suspend fun getMovies(search: String?): Movies {
        return movieApiService.getMovies(search)
    }

    suspend fun getMovie(id: Int): Movie {
        return movieApiService.getMovie(id)
    }

    suspend fun addMovie(movie: Movie): Movie {
        return movieApiService.addMovie(movie)
    }
}