package org.themoviedb.screens.movie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.adapter.MovieListAdapter
import org.themoviedb.databinding.FragmentMoviesBinding
import org.themoviedb.screens.main.view.BottomNavigationFragment
import org.themoviedb.screens.movie.viewmodel.MoviesViewModel
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class MoviesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoviesViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentMoviesBinding

    private val parent by lazy { requireParentFragment().parentFragment as BottomNavigationFragment }

    private val adapter by lazy { MovieListAdapter { movie -> parent.navigateToDetail(movie) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                errorLayout.retryButton.setOnClickListener { retryLoadMovie() }
                viewModel = this@MoviesFragment.viewModel
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = this@MoviesFragment.adapter
                }
            }
        return binding.root
    }

    private fun retryLoadMovie() = viewModel.getPopularMovies()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        observe(viewModel.getMovies()) { movies -> adapter.loadItems(movies) }
    }
}