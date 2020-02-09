package org.themoviedb.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.data.local.room.dao.MovieDao
import org.themoviedb.data.local.room.dao.TvShowDao
import org.themoviedb.utils.MOVIE_DATABASE

@Database(
    entities = [
        Movie::class,
        TvShow::class
    ], version = 1, exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao

    companion object {

        @Volatile
        private var instance: FavoriteDatabase? = null

        /**
         * Returns an instance of Room FavoriteDatabase.
         *
         * @param context application context
         * @return The singleton LetterDatabase
         */
        fun getInstance(context: Context): FavoriteDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, FavoriteDatabase::class.java, MOVIE_DATABASE)
                    .build()
            }
        }
    }
}