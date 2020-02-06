package org.themoviedb.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.themoviedb.R
import org.themoviedb.adapters.MovieListAdapter
import org.themoviedb.databinding.FragmentMoviesBinding
import org.themoviedb.ui.base.BaseFragment
import org.themoviedb.ui.main.BottomNavigationFragment
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class MoviesFragment : BaseFragment<FragmentMoviesBinding>(R.layout.fragment_movies) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoviesViewModel by viewModels { viewModelFactory }

    private val parent by lazy { requireParentFragment().parentFragment as BottomNavigationFragment }

    private val adapter by lazy { MovieListAdapter { movie -> parent.navigateToDetail(movie) } }

    private fun retryLoadMovie() = viewModel.retryLoadMovies()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        binding.apply {
            errorLayout.retryButton.setOnClickListener { retryLoadMovie().also { Log.d("RETRY", "RETRYING in View") } }
            viewModel = this@MoviesFragment.viewModel
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@MoviesFragment.adapter
            }
        }
    }

    private fun subscribeUI() {
        observe(viewModel.movies, adapter::submitList)
    }
}