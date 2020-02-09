package org.themoviedb.favorite

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_favorite_movie.view.*
import org.themoviedb.favorite.FavoriteMovie.Companion.COLUMN_BACKGROUND
import org.themoviedb.favorite.FavoriteMovie.Companion.COLUMN_DATE
import org.themoviedb.favorite.FavoriteMovie.Companion.COLUMN_DESCRIPTION
import org.themoviedb.favorite.FavoriteMovie.Companion.COLUMN_TITLE
import org.themoviedb.favorite.FavoriteMovie.Companion.MOVIE_URI
import org.themoviedb.favorite.FavoriteMovie.Companion.getFromCursor


class MainActivity : AppCompatActivity() {

    private val adapter by lazy { FavoriteMovieAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        LoaderManager.getInstance(this).initLoader(1, null, mLoaderCallbacks)
    }




    private val mLoaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) = adapter.setFavorites(data)
        override fun onLoaderReset(loader: Loader<Cursor>) = adapter.setFavorites(null)

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                applicationContext,
                MOVIE_URI,
                arrayOf(
                    COLUMN_TITLE, COLUMN_DATE, COLUMN_DESCRIPTION, COLUMN_BACKGROUND
                ), null, null, null
            )
        }

    }

    private class FavoriteMovieAdapter : RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder>() {

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

        internal class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_favorite_movie, parent, false
            )
        ) {

            fun bind(cursor: Cursor){
                val movie = getFromCursor(cursor)
                with(itemView){
                    title.text = movie.title
                    date.text = movie.date
                }
            }
        }

    }
}
