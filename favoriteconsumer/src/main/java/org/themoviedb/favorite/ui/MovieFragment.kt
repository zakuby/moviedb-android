package org.themoviedb.favorite.ui

import android.database.Cursor
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
import org.themoviedb.favorite.adapters.FavoriteMovieAdapter
import org.themoviedb.favorite.models.FavoriteMovie

class MovieFragment : Fragment(){

    companion object{
        fun newInstance() = MovieFragment()
    }

    private val adapter by lazy { FavoriteMovieAdapter() }

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
                FavoriteMovie.MOVIE_URI,
                arrayOf(
                    FavoriteMovie.COLUMN_TITLE,
                    FavoriteMovie.COLUMN_DATE,
                    FavoriteMovie.COLUMN_DESCRIPTION,
                    FavoriteMovie.COLUMN_BACKGROUND
                ), null, null, null
            )
        }
    }

}