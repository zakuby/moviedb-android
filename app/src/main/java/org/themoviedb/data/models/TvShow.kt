package org.themoviedb.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "tvshow")
@Parcelize
data class TvShow(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String?,
    @ColumnInfo(name = "title")
    @SerializedName("original_name")val title: String?,
    @ColumnInfo(name = "date")
    @SerializedName("first_air_date") val date: String?,
    @ColumnInfo(name = "description")
    @SerializedName("overview") val description: String?,
    @ColumnInfo(name = "rate")
    @SerializedName("vote_average") val rate: String?,
    @ColumnInfo(name = "posterImage")
    @SerializedName("poster_path") val posterImage: String?,
    @ColumnInfo(name = "backgroundImage")
    @SerializedName("backdrop_path") val backgroundImage: String?
) : Parcelable {
    fun formattedDate(): String {
        date ?: return ""
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return formatter.format(parser.parse(date) ?: return "")
    }
}