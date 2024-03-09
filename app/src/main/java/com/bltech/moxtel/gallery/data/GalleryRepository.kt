package com.bltech.moxtel.gallery.data

import com.bltech.moxtel.gallery.data.model.GalleryResponse
import com.bltech.moxtel.gallery.data.model.GitHubMovie
import com.bltech.moxtel.utils.MoxtelGitHubService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import javax.inject.Inject

class GalleryRepository(
    private val remoteDataSource: MoxtelGitHubService,
    private val externalScope: CoroutineScope
) : IGalleryRepository {
    @Inject
    constructor(remoteDataSource: MoxtelGitHubService) :
            this(remoteDataSource, CoroutineScope(SupervisorJob() + Dispatchers.IO))

    override suspend fun getMovies(): GalleryResponse {
        return externalScope.async {
            remoteDataSource.getMovies() //TODO : Add to cache.
        }.await()
    }

    override suspend fun getMovie(id: Int): GitHubMovie? {
        return externalScope.async {
            remoteDataSource.getMovies().movies?.firstOrNull { it.id == id } //TODO : From cache.
        }.await()
    }

    override suspend fun getSimilarMovies(movieId: Int, count: Int): List<GitHubMovie> {
        return externalScope.async {
            val genre = getMovie(movieId)?.genres?.shuffled()?.firstOrNull()
            if (genre == null) {
                emptyList()
            } else {
                remoteDataSource.getMovies().movies?.filter { it.genres?.contains(genre) ?: false }
                    ?.shuffled()
                    ?.take(5) ?: emptyList()
            }
        }.await()
    }
}

interface IGalleryRepository {
    suspend fun getMovies(): GalleryResponse
    suspend fun getMovie(id: Int): GitHubMovie?
    suspend fun getSimilarMovies(movieId: Int, count: Int = 5): List<GitHubMovie>
}
