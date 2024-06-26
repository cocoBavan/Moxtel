package com.bltech.moxtel.features.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bltech.moxtel.R
import com.bltech.moxtel.features.domain.model.Movie
import com.bltech.moxtel.features.ui.MovieCellView
import com.bltech.moxtel.features.ui.details.state.DetailsUIState
import com.bltech.moxtel.features.ui.home.model.MovieCellUIModel
import com.bltech.moxtel.global.TitleSetter
import com.bltech.moxtel.global.navigation.MoxRoutes
import com.bltech.moxtel.global.theme.MoxtelTheme
import com.bltech.moxtel.global.util.unwrapped


@Composable
fun DetailsScreen(
    movieId: Int,
    navController: NavHostController,
    titleSetter: TitleSetter,
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchMovie(movieId)
    }
    when (val uiState = viewModel.movieFlow.collectAsStateWithLifecycle().value) {
        is DetailsUIState.Loading -> {
            Text(
                text = stringResource(id = R.string.details_loading),
                modifier = Modifier.testTag("loading")
            )
        }

        DetailsUIState.Error -> {
            Text(
                text = stringResource(id = R.string.details_error),
                modifier = Modifier.testTag("error")
            )
        }

        is DetailsUIState.Success -> {
            DetailsView(uiState.movie, uiState.similarMovies, navController, titleSetter)
        }
    }
}

@Composable
fun DetailsView(
    movie: Movie,
    similarMovies: List<MovieCellUIModel>,
    navController: NavHostController,
    titleSetter: TitleSetter
) {
    LaunchedEffect(Unit) {
        titleSetter.setTitle(movie.title.unwrapped)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .testTag("detail_view")
    ) {
        val movieTitle = movie.title.unwrapped
        HeroImage(movie = movie)
        Text(text = movieTitle)
        AvailableFeatures()
        WatchNowButton(movie.id, navController)
        AddToWatchListButton()
        Text(text = movie.plot.unwrapped)
        SimilarMovies(similarMovies, navController, titleSetter)
    }
}

@Composable
fun AvailableFeatures() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            modifier = Modifier.size(dimensionResource(id = R.dimen.detail_view_icon_size)),
            painter = painterResource(id = R.drawable.hd_icon),
            contentDescription = ""
        )
        Image(
            modifier = Modifier.size(dimensionResource(id = R.dimen.detail_view_icon_size)),
            painter = painterResource(id = R.drawable.cc_icon),
            contentDescription = ""
        )
    }
}

@Composable
fun SimilarMovies(
    similarMovies: List<MovieCellUIModel>,
    navController: NavHostController,
    titleSetter: TitleSetter
) {
    if (similarMovies.isNotEmpty()) {
        Text(
            text = stringResource(id = R.string.details_similar_movies),
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow {
            items(similarMovies) {
                MovieCellView(movie = it, modifier = Modifier
                    .width(dimensionResource(id = R.dimen.detail_view_similar_movies_cell_width))
                    .clickable {
                        titleSetter.setTitle(it.title)
                        navController.navigate("${MoxRoutes.DETAILS}/${it.id}")
                    })
            }
        }
    }
}

@Composable
fun WatchNowButton(movieId: Int, navController: NavHostController) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(0.dp),
        onClick = {
            navController.navigate("${MoxRoutes.PLAYER}/$movieId")
        }) {
        Text(stringResource(id = R.string.details_watch_now))
    }
}

@Composable
fun AddToWatchListButton() {
    Button(
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(0.dp),
        onClick = {}) {
        Image(
            modifier = Modifier.size(dimensionResource(id = R.dimen.detail_view_small_icon_size)),
            painter = painterResource(id = R.drawable.plus_icon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.vertical_space_small)))
        Text(stringResource(id = R.string.details_watchlist))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailsViewPreview() {
    MoxtelTheme {
        Box(modifier = Modifier) {
            DetailsView(
                Movie(
                    id = 0,
                    title = "Blade Runner 2048",
                    posterUrl = "https://image.tmdb.org/t/p/w370_and_h556_bestv2/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    plot = null
                ),
                emptyList(),
                rememberNavController(),
                object : TitleSetter {
                    override fun setTitle(title: String) {}
                }
            )
        }
    }
}


