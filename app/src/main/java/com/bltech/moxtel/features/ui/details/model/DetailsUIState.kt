package com.bltech.moxtel.features.ui.details.model

import com.bltech.moxtel.features.data.model.GitHubMovie
import com.bltech.moxtel.features.ui.home.model.MovieCellModel

sealed class DetailsUIState {
    data class Success(val movie: GitHubMovie, val similarMovies: List<MovieCellModel>) :
        DetailsUIState()

    data object Error : DetailsUIState()
    data object Loading : DetailsUIState()
}
