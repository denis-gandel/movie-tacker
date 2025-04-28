package com.example.movietracker.api.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repository {
    private const val BASE_URL = "https://spc4skr4-3000.brs.devtunnels.ms/"
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}