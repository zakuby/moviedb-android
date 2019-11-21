package org.themoviedb.screens.tvshow.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.databinding.FragmentTvShowsBinding
import org.themoviedb.screens.movie.view.MovieDetailActivity
import org.themoviedb.screens.tvshow.viewmodel.TvShowViewModel
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class TvShowFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TvShowViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentTvShowsBinding

    private val adapter by lazy {
        TvShowsAdapter { tvShow ->
            val movieDetailIntent = Intent(activity, MovieDetailActivity::class.java)
                .apply { putExtra("movie", tvShow) }
            requireActivity().startActivity(movieDetailIntent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTvShowsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@TvShowFragment.viewModel
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = this@TvShowFragment.adapter
                }
            }
        return binding.root
    }

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

        observe(viewModel.getTvShows()) { tvShows ->
            adapter.loadTvShows(tvShows)
        }
    }
}