package com.bltech.moxtel.features.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bltech.moxtel.features.data.model.Movie
import com.bltech.moxtel.features.data.model.MovieGenre

@Database(entities = [Movie::class, MovieGenre::class], version = 1)
abstract class MovieDB : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
