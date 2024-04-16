package com.bltech.moxtel.domain.usecase

import com.bltech.moxtel.domain.contract.IMovieRepository
import com.bltech.moxtel.domain.model.Movie
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class FetchSimilarMoviesUseCase(
    private val repository: IMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFetchSimilarMoviesUseCase {
    @Inject
    constructor(repository: IMovieRepository) :
            this(
                repository,
                Dispatchers.IO
            )

    override suspend fun getSimilarMovies(movieId: Int, count: Int): List<Movie> =
        withContext(dispatcher) { repository.getSimilarMovies(movieId, count) }


}

