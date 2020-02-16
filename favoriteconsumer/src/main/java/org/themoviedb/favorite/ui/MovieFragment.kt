package org.themoviedb.favorite.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list_favorite.*
import org.themoviedb.favorite.R
import org.themoviedb.favorite.adapters.FavoriteAdapter
import org.themoviedb.favorite.models.Favorite

class MovieFragment : Fragment() {

    companion object {
        fun newInstance() = MovieFragment()
    }

    private val adapter by lazy { FavoriteAdapter(this::renderState, this::openDetail) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            adapter = this@MovieFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        LoaderManager.getInstance(this).initLoader(1, null, mLoaderMovieCallbacks)
    }

    private val mLoaderMovieCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) = adapter.setFavorites(data)
        override fun onLoaderReset(loader: Loader<Cursor>) = adapter.setFavorites(null)

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                requireContext(),
                Favorite.MOVIE_URI,
                arrayOf(
                    Favorite.COLUMN_ID,
                    Favorite.COLUMN_TITLE,
                    Favorite.COLUMN_DATE,
                    Favorite.COLUMN_DESCRIPTION,
                    Favorite.COLUMN_BACKGROUND
                ), null, null, null
            )
        }
    }

    private fun renderState(isEmpty: Boolean) {
        empty_state.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun openDetail(id: Int) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("themoviedb://detail/$id?isMovie=true"))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle exception
        }
    }
}