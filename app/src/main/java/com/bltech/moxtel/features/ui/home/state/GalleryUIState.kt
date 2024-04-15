package com.bltech.moxtel.features.ui.home.state

import com.bltech.moxtel.features.ui.home.model.MovieCellUIModel

sealed class GalleryUIState {
    data class Success(val movies: List<MovieCellUIModel>) : GalleryUIState()
    data class Error(val detail: String) : GalleryUIState()
    data object Loading : GalleryUIState()
}
