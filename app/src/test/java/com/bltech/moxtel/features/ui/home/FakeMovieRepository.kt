package com.bltech.moxtel.features.ui.home

import com.bltech.moxtel.features.domain.contract.IMovieRepository
import com.bltech.moxtel.features.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeMovieRepository : IMovieRepository {

    private var nextResult: NextResult? = null

    fun setNextResultSetOfMovies(movies: List<Movie>, genres: List<Pair<Int, String>>) {
        nextResult = NextResult.Success(movies, genres)
    }

    fun setNextResultError(exception: Exception) {
        nextResult = NextResult.Error(exception)
    }

    fun clear() {
        nextResult = null
    }

    private val flow = MutableSharedFlow<List<Movie>>()
    override suspend fun getMovie(id: Int): Movie? {
        return when (val currentResult = nextResult) {
            is NextResult.Error -> throw currentResult.exception
            is NextResult.Success -> currentResult.movies.firstOrNull { it.id == id }
            null -> null
        }
    }

    override suspend fun getSimilarMovies(movieId: Int, count: Int): List<Movie> {
        return when (val currentResult = nextResult) {
            is NextResult.Error -> emptyList()
            is NextResult.Success -> {
                val randomGenre =
                    currentResult.genrePair.shuffled().firstOrNull { it.first == movieId }?.second
                if (randomGenre == null) {
                    emptyList()
                } else {
                    val similarMovieIds =
                        currentResult.genrePair.shuffled()
                            .filter { it.second == randomGenre && it.first != movieId }
                            .map { it.first }
                    similarMovieIds.take(count).mapNotNull { getMovie(it) }
                }
            }

            null -> emptyList()
        }

    }

    override suspend fun downloadMovies() {
        when (val currentResult = nextResult) {
            is NextResult.Error -> throw currentResult.exception
            is NextResult.Success -> flow.emit(currentResult.movies)
            null -> {}
        }
    }

    override fun getMoviesFlow(): Flow<List<Movie>> = flow

    sealed class NextResult {
        class Success(val movies: List<Movie>, val genrePair: List<Pair<Int, String>>) :
            NextResult()

        class Error(val exception: Exception) : NextResult()
    }


}
