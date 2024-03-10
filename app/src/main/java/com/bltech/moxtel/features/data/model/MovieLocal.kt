package com.bltech.moxtel.features.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieLocal(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String?,
    val plot: String?
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = MovieLocal::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("movieId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class MovieGenre(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val genre: String,
    val movieId: Int
)





