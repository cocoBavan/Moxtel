package com.bltech.moxtel.features.ui.details

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bltech.moxtel.features.data.datasource.local.MovieDB
import com.bltech.moxtel.features.data.datasource.local.MovieDao
import com.bltech.moxtel.features.data.model.MovieGenre
import com.bltech.moxtel.features.data.model.MovieLocal
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {
    private lateinit var movieDao: MovieDao
    private lateinit var db: MovieDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MovieDB::class.java
        ).build()
        movieDao = db.movieDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeMoviesAndFindSimilarMovies() = runTest {
        val movies = listOf(
            MovieLocal(0, "A", null, null),
            MovieLocal(1, "B", null, null),
            MovieLocal(2, "C", null, null),
            MovieLocal(3, "D", null, null),
            MovieLocal(4, "E", null, null),
            MovieLocal(5, "F", null, null)
        )
        val genres = listOf(
            Pair(0, "GA"),

            Pair(1, "GC"),
            Pair(1, "GD"),

            Pair(2, "GC"),
            Pair(2, "GD"),

            Pair(3, "GC"),
            Pair(3, "GC"),

            Pair(4, "GC"),
            Pair(4, "GD"),

            Pair(5, "GD"),
            Pair(5, "GE"),
            Pair(5, "GA"),
        )

        movieDao.insertMovies(movies)
        movieDao.insertGenres(genres.mapIndexed { index, pair ->
            MovieGenre(genre = pair.second, movieId = pair.first)
        }
        )

        val similarMovies = movieDao.getSimilarMovies(0, 5)
        assertEquals(1, similarMovies.count())
        assertEquals(5, similarMovies[0].id)
    }
}
