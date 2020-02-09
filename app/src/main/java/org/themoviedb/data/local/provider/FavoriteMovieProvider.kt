package org.themoviedb.data.local.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.room.FavoriteDatabase
import org.themoviedb.data.local.room.dao.MovieDao
import org.themoviedb.utils.PROVIDER_AUTHORITY

class FavoriteMovieProvider : ContentProvider() {

    private lateinit var movieDao: MovieDao

    companion object{
        private val MOVIE_URI = Uri.parse("content://$PROVIDER_AUTHORITY/${Movie.TABLE_NAME}")
        private const val CODE_MOVIE_DIR = 1
        private const val CODE_MOVIE_ITEM = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(PROVIDER_AUTHORITY, Movie.TABLE_NAME, CODE_MOVIE_DIR)
            MATCHER.addURI(PROVIDER_AUTHORITY, "${Movie.TABLE_NAME}/*", CODE_MOVIE_ITEM)
        }
    }


    override fun onCreate(): Boolean {
        movieDao = FavoriteDatabase.getInstance(requireNotNull(context)).movieDao()
        return true
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (MATCHER.match(uri)) {
            CODE_MOVIE_DIR -> {
                val context = context ?: return null
                val id = movieDao.insertCursor(Movie.fromContentValues(values))
                context.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
            CODE_MOVIE_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        if (code == CODE_MOVIE_DIR || code == CODE_MOVIE_ITEM) {
            val context = context ?: return null
            val cursor: Cursor = if (code == CODE_MOVIE_DIR) {
                movieDao.selectAllCursor()
            } else {
                movieDao.selectByIdCursor(ContentUris.parseId(uri).toInt())
            }
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {

        when (MATCHER.match(uri)) {
            CODE_MOVIE_DIR -> throw IllegalArgumentException("Invalid URI, cannot update without ID$uri")
            CODE_MOVIE_ITEM -> {
                val context = context ?: return 0
                val movie = Movie.fromContentValues(values).copy(id = ContentUris.parseId(uri).toInt())
                val count = movieDao.update(movie)
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (MATCHER.match(uri)) {
            CODE_MOVIE_DIR -> throw IllegalArgumentException("Invalid URI, cannot update without ID$uri")
            CODE_MOVIE_ITEM -> {
                val context = context ?: return 0
                val count = movieDao
                    .deleteByIdCursor(ContentUris.parseId(uri).toInt())
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (MATCHER.match(uri)){
            CODE_MOVIE_DIR ->  "vnd.android.cursor.dir/" + PROVIDER_AUTHORITY + "." + Movie.TABLE_NAME
            CODE_MOVIE_ITEM ->  "vnd.android.cursor.item/" + PROVIDER_AUTHORITY + "." + Movie.TABLE_NAME
            else -> throw  IllegalArgumentException("Unknown URI: $uri")
        }
    }
}