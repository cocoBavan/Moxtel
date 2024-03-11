package com.bltech.moxtel.features.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bltech.moxtel.features.data.model.MovieGenre
import com.bltech.moxtel.features.data.model.MovieLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(users: List<MovieLocal>)

    @Insert
    fun insertGenres(users: List<MovieGenre>)

    @Query("SELECT * FROM movie")
    fun getAllMovies(): Flow<List<MovieLocal>>

    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovie(id: Int): MovieLocal?

    @Query("DELETE FROM movie")
    fun deleteAllMovies()

    @Query(
        """
        SELECT m.* FROM movie m
        WHERE m.id IN (
            SELECT movieId FROM MovieGenre 
            WHERE genre = (
                SELECT genre FROM MovieGenre WHERE movieId = :movieId
                ORDER BY RANDOM() LIMIT 1
            ) AND m.id != :movieId
        ) 
        ORDER BY RANDOM() LIMIT :count
    """
    )
    suspend fun getSimilarMovies(movieId: Int, count: Int): List<MovieLocal>
}
