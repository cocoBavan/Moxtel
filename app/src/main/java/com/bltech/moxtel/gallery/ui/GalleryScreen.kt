package com.bltech.moxtel.gallery.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.bltech.moxtel.R
import com.bltech.moxtel.gallery.data.IGalleryRepository
import com.bltech.moxtel.gallery.data.model.GalleryResponse
import com.bltech.moxtel.gallery.data.model.GitHubMovie
import com.bltech.moxtel.gallery.ui.model.GalleryMovieModel
import com.bltech.moxtel.gallery.ui.model.GalleryUIState
import com.bltech.moxtel.global.navigation.MoxRoutes
import com.bltech.moxtel.global.ui.theme.MoxtelTheme

@Composable
fun GalleryScreen(viewModel: GalleryViewModel = hiltViewModel(), navController: NavController) {
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
fun GalleryView(movies: List<GalleryMovieModel>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(movies.count(), key = {
            movies[it].id
        }) { index ->
            GalleryItemView(movies[index], modifier = Modifier.clickable {
                navController.navigate("${MoxRoutes.DETAILS}/${movies[index].id}")
            })
        }
    }
}

@Composable
fun GalleryItemView(movie: GalleryMovieModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.galley_view_text_box_padding))
            .border(
                width = dimensionResource(id = R.dimen.galley_view_cell_border_size),
                color = Color.Black,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.galley_view_cell_rounded_corner))
            )
    ) {
        Box(
            modifier
                .aspectRatio(ratio = 1f)
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.galley_view_cell_rounded_corner)))
                .fillMaxSize(),
            contentAlignment = Alignment.BottomStart

        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .aspectRatio(ratio = 1f),
                model = movie.posterUrl,
                contentDescription = stringResource(
                    id = R.string.galley_view_item_content_description,
                    movie.title
                ),
                contentScale = ContentScale.FillWidth,
                clipToBounds = true,
            ) {
                val state = painter.state
                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(Modifier.size(dimensionResource(id = R.dimen.galley_view_cell_progress_view_size)))
                        }
                    }

                    is AsyncImagePainter.State.Error -> {
                        Image(
                            painter = painterResource(id = R.drawable.movie_placeholder),
                            contentDescription = stringResource(
                                id = R.string.galley_view_item_content_description,
                                movie.title
                            )
                        )
                    }

                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.6f))
                    .padding(dimensionResource(id = R.dimen.galley_view_text_box_padding))
            ) {
                Text(
                    text = movie.title,
                    fontSize = dimensionResource(id = R.dimen.galley_view_title_size).value.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

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
        GalleryScreen(
            viewModel = GalleryViewModel(repository = object : IGalleryRepository {
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
            }),
            navController = navController
        )
    }
}
