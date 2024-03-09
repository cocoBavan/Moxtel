package com.bltech.moxtel.features.data.repository

import com.bltech.moxtel.features.data.datasource.remote.MoxtelGitHubService
import com.bltech.moxtel.features.data.model.GalleryResponse
import com.bltech.moxtel.features.data.model.GitHubMovie
import com.bltech.moxtel.features.domain.contract.IMovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import javax.inject.Inject

class MoviesRepository(
    private val remoteDataSource: MoxtelGitHubService,
    private val externalScope: CoroutineScope
) : IMovieRepository {
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
                remoteDataSource.getMovies().movies?.filter {
                    it.id != movieId && it.genres?.contains(
                        genre
                    ) ?: false
                }
                    ?.shuffled()
                    ?.take(count) ?: emptyList()
            }
        }.await()
    }
}

