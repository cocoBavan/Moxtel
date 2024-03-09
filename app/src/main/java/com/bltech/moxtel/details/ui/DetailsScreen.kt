package com.bltech.moxtel.details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.bltech.moxtel.R
import com.bltech.moxtel.gallery.data.model.GitHubMovie
import com.bltech.moxtel.gallery.ui.GalleryItemView
import com.bltech.moxtel.gallery.ui.model.MovieCellDataModel
import com.bltech.moxtel.global.ui.theme.MoxtelTheme


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
            DetailsView(uiState.movie, uiState.similarMovies)
        }
    }
}

@Composable
fun DetailsView(movie: GitHubMovie, similarMovies: List<MovieCellDataModel>) {
    val movieTitle = movie.title ?: "Unknown"
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .aspectRatio(ratio = 16 / 9.0f)
                .fillMaxWidth(),
            model = movie.posterUrl,
            contentDescription = stringResource(
                id = R.string.galley_view_item_content_description,
                movieTitle
            ),
            contentScale = ContentScale.FillWidth,
            clipToBounds = true,
        ) {
            val state = painter.state
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(Modifier.size(dimensionResource(id = R.dimen.galley_view_cell_progress_view_size)))
                    }
                }

                is AsyncImagePainter.State.Error -> {
                    Image(
                        painter = painterResource(id = R.drawable.movie_placeholder),
                        contentDescription = stringResource(
                            id = R.string.galley_view_item_content_description,
                            movieTitle
                        )
                    )
                }

                else -> {
                    SubcomposeAsyncImageContent()
                }
            }
        }
        Text(text = movieTitle)
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
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(0.dp),
            onClick = { }) {
            Text("Watch Now")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            shape = RoundedCornerShape(0.dp),
            onClick = {}) {
            Image(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.plus_icon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(colorResource(id = R.color.white))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Watchlist")
        }

        Text(text = movie.plot ?: "")

        if (similarMovies.isNotEmpty()) {
            Text(
                text = "Similar Movies",
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow {
                items(similarMovies) {
                    GalleryItemView(movie = it, modifier = Modifier.width(200.dp))
                }
            }
        }
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
            emptyList()
        )
    }
}
