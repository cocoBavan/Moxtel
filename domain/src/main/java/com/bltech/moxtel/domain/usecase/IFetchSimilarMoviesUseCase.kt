package com.bltech.moxtel.domain.usecase

import com.bltech.moxtel.domain.model.Movie

interface IFetchSimilarMoviesUseCase {
    suspend fun getSimilarMovies(movieId: Int, count: Int = 5): List<Movie>
}

