package org.themoviedb.screens.favorite.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.adapter.FavoriteMovieAdapter
import org.themoviedb.data.models.Movie
import org.themoviedb.databinding.FragmentFavoriteListBinding
import org.themoviedb.screens.favorite.viewmodel.FavoriteMovieViewModel
import org.themoviedb.utils.CustomDialog
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class FavoriteMovieFragment : DaggerFragment() {

    companion object {
        fun newInstance(): FavoriteMovieFragment = FavoriteMovieFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var customDialog: CustomDialog

    private val viewModel: FavoriteMovieViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentFavoriteListBinding

    private val parent by lazy { requireParentFragment() as FavoriteFragment }

    private val adapter by lazy { FavoriteMovieAdapter(
        clickListener = { parent.navigateToDetail(it) },
        removeFavoriteListener = { handleRemoveMovie(it) }) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = this@FavoriteMovieFragment.adapter
                }
            }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.loadFavoritesMovieRepo()
        observe(viewModel.getFavoriteMovies()) { movies -> adapter.loadItems(movies) }
        observe(viewModel.isMovieEmpty) { isEmpty ->
            binding.apply {
                recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
                emptyAnimation.visibility = if (isEmpty) View.VISIBLE else View.GONE
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
            customDialog.showRemoveFromFavoriteDialog(requireContext(), dismissListener)
        }
    }
}