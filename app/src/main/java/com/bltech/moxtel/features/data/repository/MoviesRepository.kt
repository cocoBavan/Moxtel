package com.bltech.moxtel.features.data.repository

import com.bltech.moxtel.features.data.datasource.local.MovieDao
import com.bltech.moxtel.features.data.datasource.remote.MoxtelGitHubService
import com.bltech.moxtel.features.data.model.Movie
import com.bltech.moxtel.features.data.model.MovieGenre
import com.bltech.moxtel.features.domain.contract.IMovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository(
    private val remoteDataSource: MoxtelGitHubService,
    private val localDataSource: MovieDao,
    private val externalScope: CoroutineScope
) : IMovieRepository {
    @Inject
    constructor(remoteDataSource: MoxtelGitHubService, localDataSource: MovieDao) :
            this(
                remoteDataSource,
                localDataSource,
                CoroutineScope(SupervisorJob() + Dispatchers.IO)
            )

    override suspend fun getMovie(id: Int): Movie? {
        return externalScope.async {
            localDataSource.getMovie(id)
        }.await()
    }

    override suspend fun getSimilarMovies(movieId: Int, count: Int): List<Movie> {
        return externalScope.async {
            localDataSource.getSimilarMovies(
                movieId, count
            )
        }.await()
    }

    override suspend fun downloadMovies() {
        externalScope.async {
            localDataSource.deleteAllMovies()

            val remoteResult = remoteDataSource.getMovies().movies

            val genres = arrayListOf<MovieGenre>()
            remoteResult?.mapNotNull { movie ->
                if (movie.id != null && movie.title != null) {
                    movie.genres?.forEach {
                        genres.add(MovieGenre(movieId = movie.id, genre = it))
                    }
                    Movie(
                        id = movie.id,
                        title = movie.title,
                        posterUrl = movie.posterUrl,
                        plot = movie.plot
                    )
                } else {
                    null
                }
            }?.let {
                localDataSource.insertMovies(it)
                localDataSource.insertGenres(genres)
            }
        }.await()
    }

    override fun getMoviesFlow(): Flow<List<Movie>> = localDataSource.getAllMovies()
}

