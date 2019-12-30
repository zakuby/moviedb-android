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
import org.themoviedb.R
import org.themoviedb.adapter.FavoriteTvShowAdapter
import org.themoviedb.data.models.TvShow
import org.themoviedb.databinding.FragmentFavoriteListBinding
import org.themoviedb.screens.favorite.viewmodel.FavoriteTvShowViewModel
import org.themoviedb.utils.CustomDialog
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class FavoriteTvShowFragment : DaggerFragment() {

    companion object {
        fun newInstance(): FavoriteTvShowFragment = FavoriteTvShowFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var customDialog: CustomDialog

    private val viewModel: FavoriteTvShowViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentFavoriteListBinding

    private val parent by lazy { requireParentFragment() as FavoriteFragment }

    private val adapter by lazy {
        FavoriteTvShowAdapter(
            clickListener = { parent.navigateToDetail(it.convertToMovie()) },
            removeFavoriteListener = { handleRemoveTvShow(it) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = this@FavoriteTvShowFragment.adapter
                }
                btnAddFavorite.setOnClickListener { parent.navigateTab(R.id.tv_show_fragment) }
            }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
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
            customDialog.showRemoveFromFavoriteDialog(requireContext(), dismissListener)
        }
    }
}