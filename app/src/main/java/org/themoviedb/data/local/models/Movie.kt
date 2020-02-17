package org.themoviedb.data.local.models

import android.content.ContentValues
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import org.themoviedb.data.local.models.Movie.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
@TypeConverters(GenreConverter::class)
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = COLUMN_TITLE)
    val title: String?,
    @SerializedName("release_date")
    @ColumnInfo(name = COLUMN_DATE)
    val date: String?,
    @SerializedName("overview")
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    val description: String?,
    @SerializedName("vote_average")
    @ColumnInfo(name = COLUMN_VOTE)
    val rate: String?,
    @SerializedName("poster_path")
    @ColumnInfo(name = COLUMN_POSTER)
    val posterImage: String?,
    @SerializedName("backdrop_path")
    @ColumnInfo(name = COLUMN_BACKGROUND)
    val backgroundImage: String?,
    @ColumnInfo(name = COLUMN_IS_MOVIE)
    val isMovie: Boolean? = true,
    @ColumnInfo(name = COLUMN_IS_FAVORITE)
    var isFavorite: Boolean = false,
    val genres: List<Genre>? = emptyList()
) {

    companion object {

        const val TABLE_NAME = "movie"
        private const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_VOTE = "rate"
        const val COLUMN_POSTER = "posterImage"
        const val COLUMN_BACKGROUND = "backgroundImage"
        const val COLUMN_IS_MOVIE = "isMovie"
        const val COLUMN_IS_FAVORITE = "isFavorite"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }

        fun fromContentValues(values: ContentValues?): Movie {
            values?.let {
                return Movie(
                    id = values.getAsInteger(COLUMN_ID),
                    title = values.getAsString(COLUMN_TITLE),
                    date = values.getAsString(COLUMN_DATE),
                    description = values.getAsString(COLUMN_DESCRIPTION),
                    rate = values.getAsString(COLUMN_VOTE),
                    posterImage = values.getAsString(COLUMN_POSTER),
                    backgroundImage = values.getAsString(COLUMN_BACKGROUND),
                    isFavorite = values.getAsBoolean(COLUMN_IS_FAVORITE) ?: true
                )
            } ?: throw IllegalArgumentException("Content Values is empty")
        }
    }

    fun convertToTvShow(): TvShow = TvShow(
        id = id, title = title, date = date, description = description, rate = rate,
        posterImage = posterImage, backgroundImage = backgroundImage, isFavorite = isFavorite
    )
}