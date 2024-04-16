package com.bltech.moxtel.domain.usecase

import com.bltech.moxtel.domain.model.Movie

interface IFetchTheMovieUseCase {
    suspend fun getMovie(id: Int): Movie?
}
