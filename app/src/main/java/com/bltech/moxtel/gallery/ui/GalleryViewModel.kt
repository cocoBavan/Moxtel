package com.bltech.moxtel.gallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bltech.moxtel.gallery.data.IGalleryRepository
import com.bltech.moxtel.gallery.data.model.GitHubMovie
import com.bltech.moxtel.gallery.ui.model.GalleryMovieModel
import com.bltech.moxtel.gallery.ui.model.GalleryUIState
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
    private val repository: IGalleryRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ViewModel() {

    @Inject
    constructor(repository: IGalleryRepository) : this(repository, Dispatchers.IO)

    private var _moviesFlow = MutableStateFlow<GalleryUIState>(GalleryUIState.Loading)
    val movieFlow = _moviesFlow.asStateFlow()
    fun fetchMovies() {
        _moviesFlow.value = GalleryUIState.Loading
        viewModelScope.launch(dispatcher) {
            try {
                val movies = repository.getMovies().movies?.mapNotNull(mapperDataToUI)
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

    private val mapperDataToUI: (GitHubMovie) -> GalleryMovieModel? = { movie ->
        if (movie.id != null && movie.title != null && movie.posterUrl != null) {
            GalleryMovieModel(
                id = movie.id,
                title = movie.title,
                posterUrl = movie.posterUrl
            )
        } else {
            null
        }
    }
}



