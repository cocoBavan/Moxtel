package com.bltech.moxtel.features.domain.usecase

import com.bltech.moxtel.features.domain.model.Movie

interface IFetchTheMovieUseCase {
    suspend fun getMovie(id: Int): Movie?
}
