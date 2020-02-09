package org.themoviedb.favorite

import android.database.Cursor
import android.net.Uri

data class FavoriteMovie(
    val title: String?,
    val date: String?,
    val description: String?,
    val backgroundImage: String?
){


    companion object {
        val MOVIE_URI: Uri = Uri.parse("content://org.themoviedb.data.local.provider/movie")
        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_BACKGROUND = "backdrop_path"


        fun getFromCursor(cursor: Cursor): FavoriteMovie =
            FavoriteMovie(
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                backgroundImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BACKGROUND))
            )
    }
}