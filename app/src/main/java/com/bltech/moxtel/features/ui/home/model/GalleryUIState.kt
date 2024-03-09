package com.bltech.moxtel.features.ui.home.model

sealed class GalleryUIState {
    data class Success(var movies: List<MovieCellModel>) : GalleryUIState()
    data class Error(var detail: String) : GalleryUIState()
    data object Loading : GalleryUIState()
}
