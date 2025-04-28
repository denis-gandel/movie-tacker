package com.example.movietracker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.movietracker.db.models.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM Movie")
    suspend fun getMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM Movie WHERE id = :id")
    suspend fun getMovie(id: Int): Movie?

    @Query("SELECT * FROM Movie WHERE isFavorite = 1")
    suspend fun getFavoriteMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE isWatched = 1")
    suspend fun getWatchedMovies(): List<Movie>

    @Update
    suspend fun updateMovie(movie: Movie)

    @Query("UPDATE Movie SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateMovieFavorite(id: Int, isFavorite: Boolean)

    @Query("UPDATE Movie SET isWatched = :isWatched WHERE id = :id")
    suspend fun updateMovieWatched(id: Int, isWatched: Boolean)

    @Delete
    suspend fun deleteMovie(movie: Movie)
}
