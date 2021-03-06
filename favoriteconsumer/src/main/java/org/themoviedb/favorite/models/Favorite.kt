package org.themoviedb.favorite.models

import android.database.Cursor
import android.net.Uri

data class Favorite(
    val id: Int?,
    val title: String?,
    val date: String?,
    val description: String?,
    val backgroundImage: String?
) {

    companion object {

        val MOVIE_URI: Uri = Uri.parse("content://org.themoviedb.data.local.provider/movie")
        val TV_SHOW_URI: Uri = Uri.parse("content://org.themoviedb.data.local.provider/tvshow")

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_BACKGROUND = "backgroundImage"

        fun getFromCursor(cursor: Cursor): Favorite =
            Favorite(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                backgroundImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BACKGROUND))
            )
    }
}