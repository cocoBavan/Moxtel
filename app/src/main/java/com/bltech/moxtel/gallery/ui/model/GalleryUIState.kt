package com.bltech.moxtel.gallery.ui.model

sealed class GalleryUIState {
    data class Success(var movies: List<MovieCellDataModel>) : GalleryUIState()
    data class Error(var detail: String) : GalleryUIState()
    data object Loading : GalleryUIState()
}
