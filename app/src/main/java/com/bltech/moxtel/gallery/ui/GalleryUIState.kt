package com.bltech.moxtel.gallery.ui

sealed class GalleryUIState {
    data class Success(var movies: List<GalleryMovieModel>) : GalleryUIState()
    data object Error : GalleryUIState()
    data object Loading : GalleryUIState()
}
