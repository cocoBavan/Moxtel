package com.bltech.moxtel.features.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bltech.moxtel.features.data.model.GalleryResponse
import com.bltech.moxtel.features.data.model.GitHubMovie
import com.bltech.moxtel.features.domain.contract.IMovieRepository
import com.bltech.moxtel.features.ui.MovieCellView
import com.bltech.moxtel.features.ui.home.model.GalleryUIState
import com.bltech.moxtel.features.ui.home.model.MovieCellModel
import com.bltech.moxtel.global.navigation.MoxRoutes
import com.bltech.moxtel.global.theme.MoxtelTheme

@Composable
fun HomeScreen(viewModel: GalleryViewModel = hiltViewModel(), navController: NavController) {
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchMovies()
    }
    when (val uiState = viewModel.movieFlow.collectAsStateWithLifecycle().value) {
        is GalleryUIState.Loading -> {
            Text(text = "Loading...")
        }

        is GalleryUIState.Error -> {
            Text(text = "Error: ${uiState.detail}")
        }

        is GalleryUIState.Success -> {
            GalleryView(uiState.movies, navController)
        }
    }
}

@Composable
fun GalleryView(movies: List<MovieCellModel>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(movies.count(), key = {
            movies[it].id
        }) { index ->
            MovieCellView(movies[index], modifier = Modifier.clickable {
                navController.navigate("${MoxRoutes.DETAILS}/${movies[index].id}")
            })
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun GreetingPreview() {
    MoxtelTheme {
        val navController = rememberNavController()
        HomeScreen(
            viewModel = GalleryViewModel(repository = object : IMovieRepository {
                override suspend fun getMovies() =
                    GalleryResponse(
                        movies = (0..100).map {
                            GitHubMovie(
                                id = it,
                                posterUrl = "https://picsum.photos/200/300",
                                title = "Age Of Empires $it"
                            )
                        }
                    )

                override suspend fun getMovie(id: Int): GitHubMovie? = null
                override suspend fun getSimilarMovies(
                    movieId: Int,
                    count: Int
                ): List<GitHubMovie> = emptyList()
            }),
            navController = navController
        )
    }
}
