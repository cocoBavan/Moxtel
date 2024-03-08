package com.bltech.moxtel.gallery.data.model

data class GalleryResponse(
    val genres: List<String>? = null,
    val movies: List<GitHubMovie>? = null
)
