package org.themoviedb.data.local.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.themoviedb.data.local.models.TvShow.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
@Parcelize
data class TvShow(
    @PrimaryKey
    val id: Int,
    @SerializedName("original_name") val title: String?,
    @SerializedName("first_air_date") val date: String?,
    @SerializedName("overview") val description: String?,
    @SerializedName("vote_average") val rate: String?,
    @SerializedName("poster_path") val posterImage: String?,
    @SerializedName("backdrop_path") val backgroundImage: String?,
    var isFavorite: Boolean = false
) : Parcelable {

    companion object {

        const val TABLE_NAME = "tvshow"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShow>() {
            override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun convertToMovie(): Movie = Movie(
        id = id, title = title, date = date, description = description, rate = rate,
        posterImage = posterImage, backgroundImage = backgroundImage, isMovie = false, isFavorite = isFavorite
    )
}