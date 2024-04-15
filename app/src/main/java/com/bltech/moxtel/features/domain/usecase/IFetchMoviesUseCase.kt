package com.bltech.moxtel.features.domain.usecase

import com.bltech.moxtel.features.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IFetchMoviesUseCase {
    suspend fun downloadMovies()
    fun getMoviesFlow(): Flow<List<Movie>>
}
