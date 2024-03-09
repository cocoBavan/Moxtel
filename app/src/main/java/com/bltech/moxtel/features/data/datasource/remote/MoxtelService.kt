package com.bltech.moxtel.features.data.datasource.remote

import com.bltech.moxtel.features.data.model.GalleryResponse
import retrofit2.http.GET

interface MoxtelGitHubService {
    @GET("/erik-sytnyk/movies-list/master/db.json")
    suspend fun getMovies(): GalleryResponse
}
