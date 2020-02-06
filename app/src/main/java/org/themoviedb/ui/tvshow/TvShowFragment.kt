package org.themoviedb.ui.tvshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.R
import org.themoviedb.adapters.TvShowsAdapter
import org.themoviedb.databinding.FragmentTvShowsBinding
import org.themoviedb.ui.base.BaseFragment
import org.themoviedb.ui.main.BottomNavigationFragment
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class TvShowFragment : BaseFragment<FragmentTvShowsBinding>(R.layout.fragment_tv_shows) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TvShowViewModel by viewModels { viewModelFactory }

    private val parent by lazy { requireParentFragment().parentFragment as BottomNavigationFragment }

    private val adapter by lazy {
        TvShowsAdapter { tvShow -> parent.navigateToDetail(tvShow.convertToMovie()) }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        binding.apply {
            errorLayout.retryButton.setOnClickListener { retryLoadTvShow() }
            viewModel = this@TvShowFragment.viewModel
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@TvShowFragment.adapter
            }
        }
    }

    private fun retryLoadTvShow() = viewModel.getTopRatedTvShows()

    private fun subscribeUI() {
        observe(viewModel.getTvShows()) { tvShows -> adapter.loadItems(tvShows) }
    }
}