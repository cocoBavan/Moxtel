package com.bltech.moxtel.gallery.data.model

import com.google.gson.annotations.SerializedName

data class DummyMovie(
    val id: Int? = null,
    val image: String? = null,
    @SerializedName("imdb_url") val imdbUrl: String? = null,
    val movie: String? = null,
    val rating: Double? = null
)
