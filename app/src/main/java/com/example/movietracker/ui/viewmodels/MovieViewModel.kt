package com.example.movietracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movietracker.db.models.Movie
import com.example.movietracker.api.dtos.Movie as ApiMovie
import com.example.movietracker.api.repositories.MovieRepository
import com.example.movietracker.db.dao.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieDao: MovieDao,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movie: MutableLiveData<ApiMovie> = MutableLiveData<ApiMovie>()
    val movie: LiveData<ApiMovie> = _movie

    private val _isFavorite: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getMovie(id: Int) {
        viewModelScope.launch {
            val response = movieRepository.getMovie(id)
            _movie.postValue(response)
            val movieResponse = movieDao.getMovie(response.id)
            _isFavorite.postValue(movieResponse?.isFavorite)
        }
    }

    fun markViewed(id: Int) {
        viewModelScope.launch {
            var response: Movie? = movieDao.getMovie(id)
            if (response == null) {
                val movie = movieRepository.getMovie(id)
                val newMovie = Movie(
                    movie.id, movie.title, movie.year, movie.imageUrl, true, false
                )
                movieDao.insertMovie(newMovie)
            } else {
                response.isWatched = true
                movieDao.updateMovie(response)
            }
        }
    }

    fun addFavorite(movie: ApiMovie) {
        viewModelScope.launch {
            var response = movieDao.getMovie(movie.id)
            if (response == null) {
                response = Movie(movie.id, movie.title, movie.year, movie.imageUrl, true, false)
                movieDao.insertMovie(response)
            } else {
                response.isFavorite = true
                movieDao.updateMovie(response)
            }
        }
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        val movie = movieDao.getMovie(movieId)
        return movie?.isFavorite == true
    }

    suspend fun isWatched(movieId: Int): Boolean {
        val movie = movieDao.getMovie(movieId)
        return movie?.isWatched == true
    }

    fun toggleFavorite(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movie = movieDao.getMovie(movieId)
            movie?.let {
                val updatedMovie = it.copy(isFavorite = !it.isFavorite)
                movieDao.updateMovie(updatedMovie)
                _isFavorite.postValue(updatedMovie.isFavorite)
            }
        }
    }
}