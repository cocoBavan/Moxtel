package com.bltech.moxtel.features.ui.details.state

import com.bltech.moxtel.domain.model.Movie
import com.bltech.moxtel.features.ui.home.model.MovieCellUIModel

sealed class DetailsUIState {
    data class Success(
        val movie: com.bltech.moxtel.domain.model.Movie,
        val similarMovies: List<MovieCellUIModel>
    ) :
        DetailsUIState()

    data object Error : DetailsUIState()
    data object Loading : DetailsUIState()
}
