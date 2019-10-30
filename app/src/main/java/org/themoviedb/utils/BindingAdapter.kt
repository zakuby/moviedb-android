package org.themoviedb.utils

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import org.themoviedb.R
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat


@BindingAdapter("bind:loadImageUrl")
fun loadImageUrl(imgView: ImageView, url: String?) {
    if (url.isNullOrBlank()) return
    Picasso.get()
        .load(url)
        .placeholder(R.color.athens_gray)
        .error(R.drawable.img_dummy)
        .fit()
        .centerCrop()
        .into(imgView)
}

@BindingAdapter("bind:setProgressRating")
fun setProgressRating(progressBar: ProgressBar, stringRate: String){
    val rate = (stringRate.toDoubleOrNull() ?: 0.0) * 10
    val res = progressBar.context.resources
    progressBar.apply {
        progress = rate.toInt()
        progressDrawable = when (rate) {
            in 80.0 .. 100.0 -> ResourcesCompat.getDrawable(res, R.drawable.bg_circular_progress_green, null)
            in 51.0 .. 79.9 -> ResourcesCompat.getDrawable(res, R.drawable.bg_circular_progress_yellow, null)
            else -> ResourcesCompat.getDrawable(res, R.drawable.bg_circular_progress_red, null)
        }
    }
}