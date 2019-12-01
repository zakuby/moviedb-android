package org.themoviedb.screens.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.databinding.FragmentFavoriteTvShowsBinding
import org.themoviedb.screens.favorite.viewmodel.FavoriteTvShowViewModel
import org.themoviedb.screens.tvshow.view.TvShowsAdapter
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class FavoriteTvShowFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FavoriteTvShowViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentFavoriteTvShowsBinding

    private val adapter by lazy { TvShowsAdapter { } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTvShowsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@FavoriteTvShowFragment.viewModel
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = this@FavoriteTvShowFragment.adapter
                }
            }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {

        observe(viewModel.getTvShows()) { tvShows ->
            adapter.loadTvShows(tvShows)
        }
    }

}