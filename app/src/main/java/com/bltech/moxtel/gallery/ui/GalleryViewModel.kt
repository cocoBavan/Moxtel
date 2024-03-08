package com.bltech.moxtel.gallery.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bltech.moxtel.gallery.data.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


//@Stable
@HiltViewModel
class GalleryViewModel @Inject constructor(private val repository: GalleryRepository): ViewModel() {
    private var _moviesFlow = MutableStateFlow<GalleryUIState>(GalleryUIState.Loading)

    fun fetchMovies(){
        _moviesFlow.value = GalleryUIState.Loading
        viewModelScope.launch {
            try {
                val movies = repository.getMovies().movies
                if (!movies.isNullOrEmpty()) {
                    _moviesFlow.value = GalleryUIState.Success(movies)
                } else {
                    _moviesFlow.value = GalleryUIState.Error
                }
            } catch (e: Exception){
                _moviesFlow.value = GalleryUIState.Error
            }
        }
    }
}

