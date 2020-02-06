package org.themoviedb.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.data.local.room.dao.MovieDao
import org.themoviedb.data.local.room.dao.TvShowDao

@Database(
    entities = [
        Movie::class,
        TvShow::class
    ], version = 1, exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
}