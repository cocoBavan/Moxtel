package com.bltech.moxtel.features.ui.details.state

import com.bltech.moxtel.features.domain.model.Movie
import com.bltech.moxtel.features.ui.home.state.MovieCellModel

sealed class DetailsUIState {
    data class Success(val movie: Movie, val similarMovies: List<MovieCellModel>) :
        DetailsUIState()

    data object Error : DetailsUIState()
    data object Loading : DetailsUIState()
}
