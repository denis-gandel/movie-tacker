package com.example.movietracker.hilt.modules

import com.example.movietracker.api.repositories.MovieRepository
import com.example.movietracker.api.services.MovieApiService
import com.example.movietracker.db.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideMovieRepository(movieApiService: MovieApiService): MovieRepository {
        return MovieRepository(movieApiService)
    }
}