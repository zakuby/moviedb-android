package org.themoviedb.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tvshow")
@Parcelize
data class TvShow(
    @PrimaryKey
    val id: String,
    @SerializedName("original_name") val title: String?,
    @SerializedName("first_air_date") val date: String?,
    @SerializedName("overview") val description: String?,
    @SerializedName("vote_average") val rate: String?,
    @SerializedName("poster_path") val posterImage: String?,
    @SerializedName("backdrop_path") val backgroundImage: String?,
    var isFavorite: Boolean = false
) : Parcelable {

    fun convertToMovie(): Movie = Movie(
        id = id, title = title, date = date, description = description, rate = rate,
        posterImage = posterImage, backgroundImage = backgroundImage, isMovie = false, isFavorite = isFavorite
    )
}