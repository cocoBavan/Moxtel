package com.bltech.moxtel.domain.contract

import com.bltech.moxtel.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    suspend fun getMovie(id: Int): Movie?
    suspend fun getSimilarMovies(movieId: Int, count: Int = 5): List<Movie>
    suspend fun downloadMovies()

    fun getMoviesFlow(): Flow<List<Movie>>
}
