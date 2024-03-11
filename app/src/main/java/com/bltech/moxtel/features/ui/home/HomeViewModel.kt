package com.bltech.moxtel.features.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bltech.moxtel.features.domain.contract.IMovieRepository
import com.bltech.moxtel.features.domain.model.Movie
import com.bltech.moxtel.features.ui.home.model.GalleryUIState
import com.bltech.moxtel.features.ui.home.model.MovieCellModel
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
    private val repository: IMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ViewModel() {

    @Inject
    constructor(repository: IMovieRepository) : this(repository, Dispatchers.IO)

    private var moviesRemoteFetchStatusFlow =
        MutableStateFlow<GalleryUIState>(GalleryUIState.Loading)

    val moviesFlow by lazy {
        merge(moviesRemoteFetchStatusFlow, repository.getMoviesFlow()
            .catch {
                emit(emptyList())
            }.filter {
                it.isNotEmpty()
            }.map { movies ->
                GalleryUIState.Success(movies.mapNotNull { it.toUI() })
            }
        )
    }

    fun fetchMovies() {
        moviesRemoteFetchStatusFlow.value = GalleryUIState.Loading
        viewModelScope.launch(dispatcher) {
            try {
                repository.downloadMovies()
            } catch (e: Exception) {
                moviesRemoteFetchStatusFlow.value =
                    GalleryUIState.Error(e.localizedMessage ?: " Unknown Exception")
            }
        }
    }
}

fun Movie.toUI(): MovieCellModel? {
    return if (posterUrl != null) {
        MovieCellModel(
            id = id,
            title = title,
            posterUrl = posterUrl
        )
    } else {
        null
    }
}


