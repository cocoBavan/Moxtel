package com.bltech.moxtel.features.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bltech.moxtel.domain.model.Movie
import com.bltech.moxtel.domain.usecase.IFetchMoviesUseCase
import com.bltech.moxtel.features.ui.home.model.MovieCellUIModel
import com.bltech.moxtel.features.ui.home.state.GalleryUIState
import com.bltech.moxtel.global.util.unwrapped
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject


//@Stable
@HiltViewModel
class HomeViewModel(
    private val useCase: IFetchMoviesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ViewModel() {

    @Inject
    constructor(useCase: IFetchMoviesUseCase) : this(
        useCase,
        Dispatchers.IO
    )

    private var moviesRemoteFetchStatusFlow =
        MutableStateFlow<GalleryUIState>(GalleryUIState.Loading)

    val moviesFlow by lazy {
        merge(moviesRemoteFetchStatusFlow, useCase.getMoviesFlow()
            .filter {
                it.isNotEmpty()
            }.map { movies ->
                GalleryUIState.Success(movies.mapNotNull { it.toUI() })
            }.catch {
                GalleryUIState.Error(it.localizedMessage ?: "Unknown Error")
            }
        )
    }

    fun fetchMovies() {
        moviesRemoteFetchStatusFlow.value = GalleryUIState.Loading
        viewModelScope.launch(dispatcher) {
            try {
                useCase.downloadMovies()
            } catch (e: Exception) {
                moviesRemoteFetchStatusFlow.value =
                    GalleryUIState.Error(e.localizedMessage ?: " Unknown Exception")
            }
        }
    }
}

fun Movie.toUI(): MovieCellUIModel? {
    return if (posterUrl != null) {
        MovieCellUIModel(
            id = id,
            title = title,
            posterUrl = posterUrl.unwrapped
        )
    } else {
        null
    }
}


