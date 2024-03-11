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
import com.bltech.moxtel.features.domain.contract.IMovieRepository
import com.bltech.moxtel.features.domain.model.Movie
import com.bltech.moxtel.features.ui.MovieCellView
import com.bltech.moxtel.features.ui.home.model.GalleryUIState
import com.bltech.moxtel.features.ui.home.model.MovieCellModel
import com.bltech.moxtel.global.TitleSetter
import com.bltech.moxtel.global.navigation.MoxRoutes
import com.bltech.moxtel.global.theme.MoxtelTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
    titleSetter: TitleSetter
) {
    LaunchedEffect(Unit) {
        titleSetter.setTitle("Home")
        viewModel.fetchMovies()
    }
    when (val uiState =
        viewModel.moviesFlow.collectAsStateWithLifecycle(GalleryUIState.Loading).value) {
        is GalleryUIState.Loading -> {
            Text(text = "Loading...")
        }

        is GalleryUIState.Error -> {
            Text(text = "Error: ${uiState.detail}")
        }

        is GalleryUIState.Success -> {
            GalleryView(uiState.movies, navController, titleSetter)
        }
    }
}

@Composable
fun GalleryView(
    movies: List<MovieCellModel>,
    navController: NavController,
    titleSetter: TitleSetter
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(movies.count(), key = {
            movies[it].id
        }) { index ->
            MovieCellView(movies[index], modifier = Modifier.clickable {
                val movie = movies[index]
                titleSetter.setTitle(movie.title)
                navController.navigate("${MoxRoutes.DETAILS}/${movie.id}")
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
            viewModel = HomeViewModel(repository = object : IMovieRepository {
                override suspend fun getMovie(id: Int): Movie? = null
                override suspend fun getSimilarMovies(
                    movieId: Int,
                    count: Int
                ): List<Movie> = emptyList()

                override suspend fun downloadMovies() {}
                override fun getMoviesFlow(): Flow<List<Movie>> = flow {
                    delay(1000)
                    emit((0..100).map {
                        Movie(
                            id = it,
                            posterUrl = "https://picsum.photos/200/300",
                            title = "Age Of Empires $it",
                            plot = null
                        )
                    })
                }
            }),
            navController = navController,
            titleSetter = object : TitleSetter {
                override fun setTitle(title: String) {
                }
            }
        )
    }
}
