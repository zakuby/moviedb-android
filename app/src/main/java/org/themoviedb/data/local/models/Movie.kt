package org.themoviedb.data.local.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "movie")
@Parcelize
data class Movie(
    @PrimaryKey
    val id: String,
    val title: String?,
    @SerializedName("release_date") val date: String?,
    @SerializedName("overview") val description: String?,
    @SerializedName("vote_average") val rate: String?,
    @SerializedName("poster_path") val posterImage: String?,
    @SerializedName("backdrop_path") val backgroundImage: String?,
    val isMovie: Boolean? = true,
    var isFavorite: Boolean = false
) : Parcelable {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun convertToTvShow(): TvShow = TvShow(
        id = id, title = title, date = date, description = description, rate = rate,
        posterImage = posterImage, backgroundImage = backgroundImage, isFavorite = isFavorite
    )
}