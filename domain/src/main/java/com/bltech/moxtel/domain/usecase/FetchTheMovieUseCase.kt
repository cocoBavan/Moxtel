package com.bltech.moxtel.domain.usecase

import com.bltech.moxtel.domain.contract.IMovieRepository
import com.bltech.moxtel.domain.model.Movie
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class FetchTheMovieUseCase(
    private val repository: IMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFetchTheMovieUseCase {

    @Inject
    constructor(repository: IMovieRepository) :
            this(
                repository,
                Dispatchers.IO
            )

    override suspend fun getMovie(id: Int): Movie? = withContext(dispatcher) {
        repository.getMovie(id)
    }
}
