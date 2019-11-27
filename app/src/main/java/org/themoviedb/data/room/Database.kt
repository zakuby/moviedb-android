package org.themoviedb.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.themoviedb.data.models.Movie
import org.themoviedb.data.models.TvShow

@Database(
    entities = [
        Movie::class,
        TvShow::class
    ], version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
}