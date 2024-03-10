package com.bltech.moxtel.features.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bltech.moxtel.features.data.repository.MoviesRepository
import com.bltech.moxtel.features.ui.details.model.DetailsUIState
import com.bltech.moxtel.features.ui.home.toUI
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
    private val repository: MoviesRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    @Inject
    constructor(repository: MoviesRepository) : this(repository, Dispatchers.IO)

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
                        e.printStackTrace()
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

