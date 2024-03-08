package com.bltech.moxtel.details.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bltech.moxtel.gallery.data.model.GitHubMovie


@Composable
fun DetailsScreen(movieId: Int, viewModel: DetailsScreenViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchMovie(movieId)
    }
    when (val uiState = viewModel.movieFlow.collectAsStateWithLifecycle().value) {
        is DetailsUIState.Loading -> {
            Text(text = "Loading...")
        }

        DetailsUIState.Error -> {
            Text(text = "Error...")
        }

        is DetailsUIState.Success -> {
            DetailsView(uiState.movie)
        }
    }
}

@Composable
fun DetailsView(movie: GitHubMovie) {
    Text(text = movie.plot ?: "")
}
