package org.themoviedb.ui.favorite.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.themoviedb.R
import org.themoviedb.adapters.FavoriteTvShowAdapter
import org.themoviedb.data.local.models.TvShow
import org.themoviedb.data.local.provider.FavoritesProvider
import org.themoviedb.databinding.FragmentFavoriteListBinding
import org.themoviedb.ui.base.BaseFragment
import org.themoviedb.ui.favorite.viewmodel.FavoriteTvShowViewModel
import org.themoviedb.utils.CustomDialog
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class FavoriteTvShowFragment : BaseFragment<FragmentFavoriteListBinding>(R.layout.fragment_favorite_list) {

    companion object {
        fun newInstance(): FavoriteTvShowFragment = FavoriteTvShowFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var customDialog: CustomDialog

    private val viewModel: FavoriteTvShowViewModel by viewModels { viewModelFactory }

    private val parent by lazy { requireParentFragment() as FavoriteFragment }

    private val adapter by lazy {
        FavoriteTvShowAdapter(
            clickListener = { parent.navigateToDetail(it.convertToMovie(), false) },
            removeFavoriteListener = { handleRemoveTvShow(it) }
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
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = this@FavoriteTvShowFragment.adapter
            }
            btnAddFavorite.setOnClickListener { parent.navigateTab(R.id.tv_show_fragment) }
        }
    }

    private fun subscribeUI() {
        viewModel.loadTvShowRepo()
        observe(viewModel.getTvShows()) { tvShows -> adapter.loadItems(tvShows) }
        observe(viewModel.isTvShowEmpty) { isEmpty ->
            binding.apply {
                recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
                emptyLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }
    }

    private fun handleRemoveTvShow(tvShow: TvShow) {
        viewModel.removeTvShowFromRepo(tvShow.id)
        observe(viewModel.successRemoveEvent) {
            val dismissListener = DialogInterface.OnDismissListener {
                adapter.removeFromFavorite(tvShow)
                customDialog.dismiss()
            }
            requireContext().contentResolver.notifyChange(FavoritesProvider.TV_SHOW_URI, null)
            customDialog.showRemoveFromFavoriteDialog(requireContext(), dismissListener)
        }
    }
}