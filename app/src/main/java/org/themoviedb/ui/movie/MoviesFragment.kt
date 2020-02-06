package org.themoviedb.ui.movie

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
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
            errorLayout.retryButton.setOnClickListener { retryLoadMovie() }
            viewModel = this@MoviesFragment.viewModel
            searchView.apply {
                val query = this@MoviesFragment.viewModel.searchQuery.get()
                setQuery(query, false)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        this@MoviesFragment.viewModel.searchMovies(query)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean = false

                })
                view?.findViewById<ImageView>(R.id.search_close_btn)?.setOnClickListener {
                    this@MoviesFragment.viewModel.searchMovies("")
                    setQuery("", false)
                    isIconified = true
                }
            }
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