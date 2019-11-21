package org.themoviedb.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class TvShow(
    val id: String?,
    @SerializedName("original_name")val title: String?,
    @SerializedName("first_air_date") val date: String?,
    @SerializedName("overview") val description: String?,
    @SerializedName("vote_average") val rate: String?,
    @SerializedName("poster_path") val posterImage: String?,
    @SerializedName("backdrop_path") val backgroundImage: String?
) : Parcelable {
    fun formattedDate(): String {
        date ?: return ""
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return formatter.format(parser.parse(date) ?: return "")
    }
}