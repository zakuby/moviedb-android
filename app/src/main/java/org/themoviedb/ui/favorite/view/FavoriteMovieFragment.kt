package org.themoviedb.ui.favorite.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.themoviedb.R
import org.themoviedb.adapters.FavoriteMovieAdapter
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.provider.FavoritesProvider
import org.themoviedb.databinding.FragmentFavoriteListBinding
import org.themoviedb.ui.base.BaseFragment
import org.themoviedb.ui.favorite.viewmodel.FavoriteMovieViewModel
import org.themoviedb.utils.CustomDialog
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class FavoriteMovieFragment : BaseFragment<FragmentFavoriteListBinding>(R.layout.fragment_favorite_list) {

    companion object {
        fun newInstance(): FavoriteMovieFragment = FavoriteMovieFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var customDialog: CustomDialog

    private val viewModel: FavoriteMovieViewModel by viewModels { viewModelFactory }

    private val parent by lazy { requireParentFragment() as FavoriteFragment }

    private val adapter by lazy {
        FavoriteMovieAdapter(
            clickListener = { parent.navigateToDetail(it) },
            removeFavoriteListener = { handleRemoveMovie(it) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@FavoriteMovieFragment.adapter
            }
            btnAddFavorite.setOnClickListener { parent.navigateTab(R.id.movies_fragment) }
        }
    }

    private fun subscribeUI() {
        viewModel.loadFavoritesMovieRepo()
        observe(viewModel.getFavoriteMovies()) { movies -> adapter.loadItems(movies) }
        observe(viewModel.isMovieEmpty) { isEmpty ->
            binding.apply {
                recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
                emptyLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }
    }

    private fun handleRemoveMovie(movie: Movie) {
        viewModel.removeMovieFromRepo(movie.id)
        observe(viewModel.successRemoveEvent) {
            val dismissListener = DialogInterface.OnDismissListener {
                adapter.removeFromFavorite(movie)
                customDialog.dismiss()
            }
            requireContext().contentResolver.notifyChange(FavoritesProvider.MOVIE_URI, null)
            customDialog.showRemoveFromFavoriteDialog(requireContext(), dismissListener)
        }
    }
}