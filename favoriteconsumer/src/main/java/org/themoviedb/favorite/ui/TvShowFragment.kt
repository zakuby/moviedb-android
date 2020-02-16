package org.themoviedb.favorite.ui

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

class TvShowFragment : Fragment() {

    companion object {
        fun newInstance() = TvShowFragment()
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
            adapter = this@TvShowFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        LoaderManager.getInstance(this).initLoader(3, null, mLoaderTvShowCallbacks)
    }

    private val mLoaderTvShowCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) = adapter.setFavorites(data)
        override fun onLoaderReset(loader: Loader<Cursor>) = adapter.setFavorites(null)

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                requireContext(),
                Favorite.TV_SHOW_URI,
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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("themoviedb://detail/$id?isMovie=false"))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exception
        }
    }
}