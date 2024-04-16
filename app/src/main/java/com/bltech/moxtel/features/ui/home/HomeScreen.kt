package com.bltech.moxtel.features.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bltech.moxtel.R
import com.bltech.moxtel.domain.contract.IMovieRepository
import com.bltech.moxtel.domain.model.Movie
import com.bltech.moxtel.domain.usecase.FetchMoviesUseCase
import com.bltech.moxtel.features.ui.MovieCellView
import com.bltech.moxtel.features.ui.home.model.MovieCellUIModel
import com.bltech.moxtel.features.ui.home.state.GalleryUIState
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
    val title = LocalContext.current.resources.getString(R.string.home_title)
    LaunchedEffect(Unit) {
        titleSetter.setTitle(title)
        viewModel.fetchMovies()
    }
    val collectedState by viewModel.moviesFlow.collectAsStateWithLifecycle(GalleryUIState.Loading)
    when (val uiState = collectedState) {
        is GalleryUIState.Loading -> {
            Text(text = stringResource(id = R.string.home_loading))
        }

        is GalleryUIState.Error -> {
            Text(text = "${stringResource(id = R.string.home_error)} ${uiState.detail}")
        }

        is GalleryUIState.Success -> {
            if (uiState.movies.isEmpty()) {
                Text(text = "No movies found")
            } else {
                GalleryView(uiState.movies, navController, titleSetter)
            }
        }
    }
}

@Composable
fun GalleryView(
    movies: List<MovieCellUIModel>,
    navController: NavController,
    titleSetter: TitleSetter
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.testTag("gallery")
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
            viewModel = HomeViewModel(FetchMoviesUseCase(object :
                IMovieRepository {
                override suspend fun getMovie(id: Int): Movie? = null
                override suspend fun getSimilarMovies(
                    movieId: Int,
                    count: Int
                ): List<Movie> = emptyList()

                override suspend fun downloadMovies() {}
                override fun getMoviesFlow(): Flow<List<Movie>> =
                    flow {
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
            })),
            navController = navController,
            titleSetter = object : TitleSetter {
                override fun setTitle(title: String) {
                }
            }
        )
    }
}
