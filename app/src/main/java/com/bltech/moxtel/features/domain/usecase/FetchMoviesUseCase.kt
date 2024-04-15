package com.bltech.moxtel.features.domain.usecase

import com.bltech.moxtel.features.domain.contract.IMovieRepository
import com.bltech.moxtel.features.domain.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchMoviesUseCase(
    private val repository: IMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFetchMoviesUseCase {

    @Inject
    constructor(repository: IMovieRepository) :
            this(
                repository,
                Dispatchers.IO
            )

    override suspend fun downloadMovies() = withContext(dispatcher) { repository.downloadMovies() }
    override fun getMoviesFlow(): Flow<List<Movie>> = repository.getMoviesFlow()
}
