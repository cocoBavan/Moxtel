package com.bltech.moxtel.gallery.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Gallery(viewModel: GalleryViewModel = hiltViewModel()) {
    Text(
        text = "Hello Bavan!"
    )
}
