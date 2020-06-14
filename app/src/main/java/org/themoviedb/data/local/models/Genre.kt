package org.themoviedb.data.local.models

import androidx.room.TypeConverter
import com.google.gson.Gson

data class Genre(
    val id: Int?,
    val name: String?,
    var isChecked: Boolean = false
) {
    fun setCheck() {
        isChecked = !isChecked
    }
}

class GenreConverter {
    @TypeConverter
    fun listToJson(value: List<Genre>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Genre>? {
        val objects = Gson().fromJson(value, Array<Genre>::class.java) as Array<Genre>
        return objects.toList()
    }
}