package org.themoviedb.data.local.models

import androidx.recyclerview.widget.DiffUtil

data class Review(
    val id: String? = "",
    val author: String? = "",
    val content: String? = "",
    val url: String? = ""
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Review>() {
            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }
        }
    }
    val name: String get() = "A review by $author"
}