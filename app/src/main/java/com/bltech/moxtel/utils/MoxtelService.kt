package com.bltech.moxtel.utils

import com.bltech.moxtel.gallery.data.model.DummyMovie
import com.bltech.moxtel.gallery.data.model.GalleryResponse
import retrofit2.http.GET

interface MoxtelGitHubService {
    @GET("/erik-sytnyk/movies-list/blob/master/db.json")
    suspend fun getMovies(): GalleryResponse
}

interface MoxtelDummyService {
    @GET("/api/movies")
    suspend fun getDummyMovies(): List<DummyMovie>

    @GET("/api/movies/{id}")
    suspend fun getDummyMovie(): DummyMovie
}
