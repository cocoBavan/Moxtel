package com.bltech.moxtel.gallery.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.bltech.moxtel.R

@Composable
fun GalleryScreen(viewModel: GalleryViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchMovies()
    }
    when (val uiState = viewModel.movieFlow.collectAsStateWithLifecycle().value) {
        is GalleryUIState.Loading -> {
            Text(text = "Loading...")
        }

        GalleryUIState.Error -> {
            Text(text = "Error...")
        }

        is GalleryUIState.Success -> {
            GalleryView(uiState.movies)
        }
    }
}

@Composable
fun GalleryView(movies: List<GalleryMovieModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(movies.count(), key = {
            movies[it].id
        }) { index ->
            GalleryItemView(movies[index])
        }
    }
}

@Composable
fun GalleryItemView(movie: GalleryMovieModel) {
    Box(
        Modifier
            .aspectRatio(ratio = 1f)
            .padding(dimensionResource(id = R.dimen.galley_view_text_box_padding))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.galley_view_cell_rounded_corner)))
            .background(Color.Gray)
            .fillMaxSize()
            .border(
                width = dimensionResource(id = R.dimen.galley_view_cell_border_size),
                color = Color.Black,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.galley_view_cell_rounded_corner))
            ),
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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
