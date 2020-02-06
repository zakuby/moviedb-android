package org.themoviedb.data.local.models

import com.google.gson.annotations.SerializedName

data class Cast(
    val character: String?,
    val name: String?,
    @SerializedName("profile_path") val profileImage: String?
)