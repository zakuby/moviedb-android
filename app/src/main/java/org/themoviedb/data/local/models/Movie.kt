package org.themoviedb.data.local.models

import android.content.ContentValues
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.themoviedb.data.local.models.Movie.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
@Parcelize
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    var id: Int,
    @ColumnInfo(name = COLUMN_TITLE)
    var title: String?,
    @SerializedName("release_date")
    @ColumnInfo(name = COLUMN_DATE)
    var date: String?,
    @SerializedName("overview")
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    var description: String?,
    @SerializedName("vote_average")
    @ColumnInfo(name = COLUMN_VOTE)
    var rate: String?,
    @SerializedName("COLUMN_POSTER")
    @ColumnInfo(name = COLUMN_POSTER)
    var posterImage: String?,
    @SerializedName("backdrop_path")
    @ColumnInfo(name = COLUMN_BACKGROUND)
    var backgroundImage: String?,
    var isMovie: Boolean? = true,
    var isFavorite: Boolean = false
) : Parcelable {

    companion object {

        const val TABLE_NAME = "movie"
        private const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_VOTE = "vote_average"
        const val COLUMN_POSTER = "poster_path"
        const val COLUMN_BACKGROUND = "backdrop_path"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }

        fun fromContentValues(values: ContentValues?): Movie  {
            values?.let {
                return Movie(
                    id = values.getAsInteger(COLUMN_ID),
                    title = values.getAsString(COLUMN_TITLE),
                    date = values.getAsString(COLUMN_DATE),
                    description = values.getAsString(COLUMN_DESCRIPTION),
                    rate = values.getAsString(COLUMN_VOTE),
                    posterImage = values.getAsString(COLUMN_POSTER),
                    backgroundImage = values.getAsString(COLUMN_BACKGROUND),
                    isFavorite = true,
                    isMovie = true
                )
            } ?: throw IllegalArgumentException("Content Values is empty")
        }
    }

    fun convertToTvShow(): TvShow = TvShow(
        id = id, title = title, date = date, description = description, rate = rate,
        posterImage = posterImage, backgroundImage = backgroundImage, isFavorite = isFavorite
    )
}