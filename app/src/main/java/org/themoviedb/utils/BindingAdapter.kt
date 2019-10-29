package org.themoviedb.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import org.themoviedb.R

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