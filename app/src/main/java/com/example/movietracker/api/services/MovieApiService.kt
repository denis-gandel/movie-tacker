package com.example.movietracker.api.services

import com.example.movietracker.api.dtos.Movie
import com.example.movietracker.api.dtos.Movies
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movies")
    suspend fun getMovies(@Query("search") search: String?): Movies

    @GET("movies/{id}")
    suspend fun getMovie(@Path("id") id: Int): Movie

    @POST("movies")
    suspend fun addMovie(@Body movie: Movie): Movie

}