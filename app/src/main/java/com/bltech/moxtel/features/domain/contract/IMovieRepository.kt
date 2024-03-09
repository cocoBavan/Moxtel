package com.bltech.moxtel.features.domain.contract

import com.bltech.moxtel.features.data.model.GalleryResponse
import com.bltech.moxtel.features.data.model.GitHubMovie

interface IMovieRepository {
    suspend fun getMovies(): GalleryResponse
    suspend fun getMovie(id: Int): GitHubMovie?
    suspend fun getSimilarMovies(movieId: Int, count: Int = 5): List<GitHubMovie>
}
