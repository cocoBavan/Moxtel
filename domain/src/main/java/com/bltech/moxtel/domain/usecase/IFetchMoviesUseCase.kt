package com.bltech.moxtel.domain.usecase

import com.bltech.moxtel.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IFetchMoviesUseCase {
    suspend fun downloadMovies()
    fun getMoviesFlow(): Flow<List<Movie>>
}
