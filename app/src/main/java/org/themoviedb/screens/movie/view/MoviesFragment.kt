package org.themoviedb.screens.movie.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.databinding.FragmentMoviesBinding
import org.themoviedb.screens.main.view.DetailActivity
import org.themoviedb.screens.movie.viewmodel.MoviesViewModel
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class MoviesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoviesViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentMoviesBinding

    private val adapter by lazy {
        MoviesListAdapter { movie ->
            val movieDetailIntent = Intent(activity, DetailActivity::class.java)
                .apply { putExtra(DetailActivity.EXTRA_DETAIL, movie) }
            requireActivity().startActivity(movieDetailIntent)
        }
    }

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
                    layoutManager = LinearLayoutManager(requireActivity())
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

        observe(viewModel.getLoading()) { isLoading ->
            if (isLoading) {
                binding.apply {
                    recyclerView.visibility = View.GONE
                    shimmerView.visibility = View.VISIBLE
                    shimmerView.startShimmer()
                }
            } else {
                binding.apply {
                    shimmerView.stopShimmer()
                    shimmerView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }

        observe(viewModel.getMovies()) { movies ->
            adapter.loadMovies(movies)
        }
    }
}