package com.bltech.moxtel.gallery.data

import com.bltech.moxtel.gallery.data.model.GalleryResponse
import com.bltech.moxtel.utils.MoxtelGitHubService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import javax.inject.Inject

class GalleryRepository(
    private val remoteDataSource: MoxtelGitHubService,
    private val externalScope: CoroutineScope
) {
    @Inject constructor(remoteDataSource: MoxtelGitHubService):
            this (remoteDataSource, CoroutineScope(SupervisorJob() + Dispatchers.IO))
    suspend fun getMovies() : GalleryResponse {
        return externalScope.async {
            remoteDataSource.getMovies() //TODO : Add to cache.
        }.await()
    }
}
