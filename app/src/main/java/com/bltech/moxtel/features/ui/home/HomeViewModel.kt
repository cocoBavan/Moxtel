package com.bltech.moxtel.features.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bltech.moxtel.features.domain.contract.IMovieRepository
import com.bltech.moxtel.features.data.model.GitHubMovie
import com.bltech.moxtel.features.ui.home.model.GalleryUIState
import com.bltech.moxtel.features.ui.home.model.MovieCellModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


//@Stable
@HiltViewModel
class GalleryViewModel(
    private val repository: IMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ViewModel() {

    @Inject
    constructor(repository: IMovieRepository) : this(repository, Dispatchers.IO)

    private var _moviesFlow = MutableStateFlow<GalleryUIState>(GalleryUIState.Loading)
    val movieFlow = _moviesFlow.asStateFlow()
    fun fetchMovies() {
        _moviesFlow.value = GalleryUIState.Loading
        viewModelScope.launch(dispatcher) {
            try {
                val movies = repository.getMovies().movies?.mapNotNull { it.toUI() }
                if (!movies.isNullOrEmpty()) {
                    _moviesFlow.value = GalleryUIState.Success(movies)
                } else {
                    _moviesFlow.value = GalleryUIState.Error("Movie Response is null or Empty")
                }
            } catch (e: Exception) {
                print(e.stackTrace)
                _moviesFlow.value = GalleryUIState.Error(e.localizedMessage ?: " Unknown Exception")
            }
        }
    }
}

fun GitHubMovie.toUI(): MovieCellModel? {
    return if (id != null && title != null && posterUrl != null) {
        MovieCellModel(
            id = id,
            title = title,
            posterUrl = posterUrl
        )
    } else {
        null
    }
}



