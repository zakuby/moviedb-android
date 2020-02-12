package org.themoviedb.favorite.adapters

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_favorite.view.*
import org.themoviedb.favorite.R
import org.themoviedb.favorite.models.FavoriteTvShow

class FavoriteTvShowAdapter : RecyclerView.Adapter<FavoriteTvShowAdapter.ViewHolder>() {

    private var mCursor: Cursor? = null

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        mCursor?.run {
            if (moveToPosition(position)) holder.bind(mCursor ?: return)
        }
    }

    override fun getItemCount(): Int {
        return if (mCursor == null) 0 else mCursor!!.count
    }

    internal fun setFavorites(cursor: Cursor?) {
        mCursor = cursor
        notifyDataSetChanged()
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_favorite, parent, false
        )
    ) {

        fun bind(cursor: Cursor){
            val tvShow = FavoriteTvShow.getFromCursor(cursor)
            with(itemView){
                title.text = tvShow.title
                date.text = tvShow.date
                background_image.run {
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/original/${tvShow.backgroundImage}")
                        .placeholder(R.color.athens_gray)
                        .fit()
                        .centerCrop()
                        .into(this)
                }
            }
        }
    }

}