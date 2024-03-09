package com.bltech.moxtel.features.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.bltech.moxtel.R
import com.bltech.moxtel.features.data.model.GitHubMovie
import com.bltech.moxtel.global.util.unwrapped

@Composable
fun HeroImage(
    movie: GitHubMovie, contentDescription: String = stringResource(
        id = R.string.galley_view_item_content_description,
        movie.title.unwrapped
    )
) {
    SubcomposeAsyncImage(
        modifier = Modifier
            .aspectRatio(ratio = 16 / 9.0f)
            .fillMaxWidth(),
        model = movie.posterUrl,
        contentDescription = contentDescription,
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
                    contentDescription = contentDescription
                )
            }

            else -> {
                SubcomposeAsyncImageContent()
            }
        }
    }
}
