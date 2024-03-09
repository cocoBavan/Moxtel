package com.bltech.moxtel.details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bltech.moxtel.gallery.data.GalleryRepository
import com.bltech.moxtel.gallery.data.model.GitHubMovie
import com.bltech.moxtel.gallery.ui.model.MovieCellDataModel
import com.bltech.moxtel.gallery.ui.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel(
    private val repository: GalleryRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    @Inject
    constructor(repository: GalleryRepository) : this(repository, Dispatchers.IO)

    private var _movieFlow = MutableStateFlow<DetailsUIState>(DetailsUIState.Loading)
    val movieFlow = _movieFlow.asStateFlow()
    fun fetchMovie(id: Int) {
        _movieFlow.value = DetailsUIState.Loading
        viewModelScope.launch(dispatcher) {
            try {
                val movie = repository.getMovie(id)
                if (movie != null) {
                    _movieFlow.value = DetailsUIState.Success(movie, emptyList())
                    try {
                        val similarMovies =
                            repository.getSimilarMovies(id).mapNotNull { it.toUI() }
                        _movieFlow.update { currentState ->
                            (currentState as? DetailsUIState.Success)?.copy(similarMovies = similarMovies)
                                ?: currentState
                        }
                    } catch (e: Exception) {
                        //TODO: Pass it to logger
                    }
                } else {
                    _movieFlow.value = DetailsUIState.Error
                }

            } catch (e: Exception) {
                print(e.stackTrace)
                _movieFlow.value = DetailsUIState.Error
            }
        }
    }
}

sealed class DetailsUIState {
    data class Success(val movie: GitHubMovie, val similarMovies: List<MovieCellDataModel>) :
        DetailsUIState()

    data object Error : DetailsUIState()
    data object Loading : DetailsUIState()
}

