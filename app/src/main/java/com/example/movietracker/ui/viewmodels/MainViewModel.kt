package com.example.movietracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietracker.api.dtos.Movie as MovieDto
import com.example.movietracker.db.models.Movie
import com.example.movietracker.api.repositories.MovieRepository
import com.example.movietracker.db.dao.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieDao: MovieDao, private val movieRepository: MovieRepository
) : ViewModel() {
    private val _movies: MutableLiveData<List<Movie>> =
        MutableLiveData<List<Movie>>(arrayListOf<Movie>())
    val movies: LiveData<List<Movie>> = _movies

    fun getMovies() {
        viewModelScope.launch {
            val response = movieRepository.getMovies(null)
            val movies = response.map { it.toEntity() }

            movieDao.insertMovies(movies)

            val savedMovies = movieDao.getMovies()
            _movies.postValue(savedMovies)
        }
    }

    fun searchMovie(name: String) {
        viewModelScope.launch {
            val response = movieRepository.getMovies(name)
            val movies = response.map { it.toEntity() }
            _movies.postValue(movies)
        }
    }

    fun getFavoriteMovies() {
        viewModelScope.launch {
            val response = movieDao.getFavoriteMovies()
            _movies.postValue(response)
        }
    }

    fun getWatchedMovies() {
        viewModelScope.launch {
            val response = movieDao.getWatchedMovies()
            _movies.postValue(response)
        }
    }

    private fun MovieDto.toEntity(): Movie {
        return Movie(
            id = this.id,
            title = this.title,
            year = this.year,
            imageUrl = this.imageUrl,
            isFavorite = false,
            isWatched = false
        )
    }

}