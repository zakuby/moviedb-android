package org.themoviedb.screens.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.databinding.FragmentFavoriteMovieBinding
import org.themoviedb.screens.favorite.viewmodel.FavoriteMovieViewModel
import org.themoviedb.screens.movie.view.MoviesListAdapter
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class FavoriteMovieFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FavoriteMovieViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentFavoriteMovieBinding

    private val adapter by lazy { MoviesListAdapter { } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@FavoriteMovieFragment.viewModel
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = this@FavoriteMovieFragment.adapter
                }
            }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {

        observe(viewModel.getMovies()) { movies ->
            adapter.loadMovies(movies)
        }
    }


}