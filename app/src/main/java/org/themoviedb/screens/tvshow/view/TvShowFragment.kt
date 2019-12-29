package org.themoviedb.screens.tvshow.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.adapter.TvShowsAdapter
import org.themoviedb.databinding.FragmentTvShowsBinding
import org.themoviedb.screens.main.view.BottomNavigationFragment
import org.themoviedb.screens.tvshow.viewmodel.TvShowViewModel
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class TvShowFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TvShowViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentTvShowsBinding

    private val parent by lazy { requireParentFragment().parentFragment as BottomNavigationFragment }

    private val adapter by lazy {
        TvShowsAdapter { tvShow -> parent.navigateToDetail(tvShow.convertToMovie()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTvShowsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                errorLayout.retryButton.setOnClickListener { retryLoadTvShow() }
                viewModel = this@TvShowFragment.viewModel
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = this@TvShowFragment.adapter
                }
            }
        return binding.root
    }

    private fun retryLoadTvShow() = viewModel.getTopRatedTvShows()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        observe(viewModel.getTvShows()) { tvShows -> adapter.loadItems(tvShows) }
    }
}