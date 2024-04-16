package com.bltech.moxtel.features.domain.usecase

import com.bltech.moxtel.features.domain.model.Movie

interface IFetchSimilarMoviesUseCase {
    suspend fun getSimilarMovies(movieId: Int, count: Int = 5): List<Movie>
}

