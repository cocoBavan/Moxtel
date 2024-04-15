package com.bltech.moxtel.features.ui.home.state

sealed class GalleryUIState {
    data class Success(val movies: List<MovieCellModel>) : GalleryUIState()
    data class Error(val detail: String) : GalleryUIState()
    data object Loading : GalleryUIState()
}
