package org.themoviedb.ui.tvshow

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        TvShowsAdapter { tvShow -> parent.navigateToDetail(tvShow.convertToMovie(), false) }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        binding.apply {
            errorLayout.retryButton.setOnClickListener { retryLoadTvShows() }
            viewModel = this@TvShowFragment.viewModel
            searchView.apply {
                val query = this@TvShowFragment.viewModel.searchQuery.get()
                setQuery(query, false)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        this@TvShowFragment.viewModel.searchMovies(query)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean = false
                })
                view?.findViewById<ImageView>(R.id.search_close_btn)?.setOnClickListener {
                    this@TvShowFragment.viewModel.searchMovies("")
                    setQuery("", false)
                    isIconified = true
                }
            }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@TvShowFragment.adapter
            }
        }
    }

    private fun retryLoadTvShows() = viewModel.retryLoadTvShows()

    private fun subscribeUI() {
        observe(viewModel.tvShows, adapter::submitList)
    }
}