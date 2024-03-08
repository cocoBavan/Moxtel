package com.bltech.moxtel.gallery.ui

import com.bltech.moxtel.gallery.data.model.GitHubMovie

sealed class GalleryUIState {
    data class Success(var movies: List<GitHubMovie>): GalleryUIState()
    data object Error: GalleryUIState()
    data object Loading: GalleryUIState()
}
