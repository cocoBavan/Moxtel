package com.bltech.moxtel.features.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bltech.moxtel.R
import com.bltech.moxtel.features.data.model.GitHubMovie
import com.bltech.moxtel.features.ui.MovieCellView
import com.bltech.moxtel.features.ui.details.model.DetailsUIState
import com.bltech.moxtel.features.ui.home.model.MovieCellModel
import com.bltech.moxtel.global.LocalTitleSetter
import com.bltech.moxtel.global.navigation.MoxRoutes
import com.bltech.moxtel.global.theme.MoxtelTheme
import com.bltech.moxtel.global.util.unwrapped


@Composable
fun DetailsScreen(
    movieId: Int,
    navController: NavHostController,
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
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
            DetailsView(uiState.movie, uiState.similarMovies, navController)
        }
    }
}

@Composable
fun DetailsView(
    movie: GitHubMovie,
    similarMovies: List<MovieCellModel>,
    navController: NavHostController,
) {

    LocalTitleSetter.current(movie.title ?: "")

    val movieTitle = movie.title ?: "Unknown"
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        HeroImage(movie = movie)
        Text(text = movieTitle)
        AvailableFeatures()
        WatchNowButton(movie.id ?: -1, navController)
        AddToWatchListButton()
        Text(text = movie.plot.unwrapped)
        SimilarMovies(similarMovies, navController)
    }
}

@Composable
fun AvailableFeatures() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = R.drawable.hd_icon),
            contentDescription = ""
        )
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = R.drawable.cc_icon),
            contentDescription = ""
        )
    }
}

@Composable
fun SimilarMovies(similarMovies: List<MovieCellModel>, navController: NavHostController) {
    if (similarMovies.isNotEmpty()) {
        Text(
            text = "Similar Movies",
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow {
            items(similarMovies) {
                MovieCellView(movie = it, modifier = Modifier
                    .width(200.dp)
                    .clickable {
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
        Text("Watch Now")
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
            modifier = Modifier.size(12.dp),
            painter = painterResource(id = R.drawable.plus_icon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Watchlist")
    }
}

@Preview(widthDp = 1080, heightDp = 1920, showBackground = true, showSystemUi = true)
@Composable
fun DetailsViewPreview() {
    MoxtelTheme {
        DetailsView(
            GitHubMovie(
                title = "Blade Runner 2048",
                posterUrl = "https://image.tmdb.org/t/p/w370_and_h556_bestv2/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg"
            ),
            emptyList(),
            rememberNavController()
        )
    }
}


