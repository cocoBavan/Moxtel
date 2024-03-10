package com.bltech.moxtel.features.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bltech.moxtel.features.data.model.MovieGenre
import com.bltech.moxtel.features.data.model.MovieLocal

@Database(entities = [MovieLocal::class, MovieGenre::class], version = 1)
abstract class MovieDB : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
