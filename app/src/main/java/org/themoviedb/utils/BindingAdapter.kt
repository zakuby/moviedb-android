package org.themoviedb.utils

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import org.themoviedb.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("loadImageUrl")
fun loadImageUrl(imgView: ImageView, url: String?) {
    if (url.isNullOrBlank()) return
    Picasso.get()
        .load("https://image.tmdb.org/t/p/original/$url")
        .placeholder(R.color.athens_gray)
        .fit()
        .centerCrop()
        .into(imgView)
}

@BindingAdapter("setProgressRating")
fun setProgressRating(progressBar: ProgressBar, stringRate: String?) {
    stringRate ?: return
    val rate = (stringRate.toDoubleOrNull() ?: 0.0) * 10
    val res = progressBar.context.resources
    progressBar.apply {
        progress = rate.toInt()
        progressDrawable = when (rate) {
            in 80.0..100.0 -> ResourcesCompat.getDrawable(
                res,
                R.drawable.bg_circular_progress_green,
                null
            )
            in 51.0..79.9 -> ResourcesCompat.getDrawable(
                res,
                R.drawable.bg_circular_progress_yellow,
                null
            )
            else -> ResourcesCompat.getDrawable(res, R.drawable.bg_circular_progress_red, null)
        }
    }
}

@BindingAdapter("formatDate")
fun formatDate(textView: TextView, date: String?) {
    if (date.isNullOrBlank()) {
        textView.text = ""
        return
    }
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    val formattedDate = formatter.format(parser.parse(date) ?: "")
    textView.text = formattedDate
}