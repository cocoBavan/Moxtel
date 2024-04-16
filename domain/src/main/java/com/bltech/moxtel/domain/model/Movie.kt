package com.bltech.moxtel.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val plot: String?
)
