package org.themoviedb.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.themoviedb.R
import org.themoviedb.adapters.DetailGenreListAdapter
import org.themoviedb.adapters.DetailListCastAdapter
import org.themoviedb.adapters.DetailReviewListAdapter
import org.themoviedb.data.local.models.Review
import org.themoviedb.data.local.provider.FavoritesProvider
import org.themoviedb.databinding.FragmentDetailBinding
import org.themoviedb.ui.base.BaseFragment
import org.themoviedb.ui.detail.DetailViewModel.FavoriteAction.*
import org.themoviedb.ui.main.MainActivity
import org.themoviedb.ui.main.WebViewActivity
import org.themoviedb.utils.CustomDialog
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var customDialog: CustomDialog

    private val viewModel: DetailViewModel by viewModels { viewModelFactory }

    private val movieArgs: DetailFragmentArgs by navArgs()

    private val isMovieDetail: Boolean by lazy { movieArgs.isMovie }

    private val detailId: Int by lazy { movieArgs.id }

    private val castAdapter by lazy { DetailListCastAdapter() }

    private val genreAdapter by lazy { DetailGenreListAdapter() }

    private val reviewAdapter by lazy { DetailReviewListAdapter(this::viewFullReview) }

    private val activity: MainActivity by lazy { getActivity() as MainActivity }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        binding.apply {
            viewModel = this@DetailFragment.viewModel.apply { setMovieDetail(detailId, isMovieDetail) }
            backButton.setOnClickListener { requireActivity().onBackPressed() }
            recyclerViewCast.apply {
                layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = castAdapter
            }
            recyclerViewGenre.apply {
                layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = genreAdapter
            }
            recyclerViewReview.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = reviewAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        activity.supportActionBar?.show()
    }
    private fun subscribeUI() {
        observe(viewModel.getDetailGenre(), genreAdapter::loadItems)
        observe(viewModel.getMovieCasts(), castAdapter::loadItems)
        observe(viewModel.onReviewLiveDataReady, {
            observe(viewModel.getDetailReview(), reviewAdapter::submitList)
            observe(viewModel.isErrorReview, { isError ->
                binding.containerReview.isGone = isError
            })
            observe(viewModel.detailReviewLoading, { isLoading ->
                binding.shimmerViewReview.apply {
                    isGone = !isLoading
                    if (isLoading) startShimmer() else stopShimmer()
                }
                binding.reviewView.isGone = isLoading
            })
        })
        observe(viewModel.getFavoriteButtonAction()) { action ->
            requireContext().contentResolver.notifyChange(
                if (action.type == "movie") FavoritesProvider.MOVIE_URI else FavoritesProvider.TV_SHOW_URI, null
            )
            when (action) {
                is ActionFavoriteAdded -> customDialog.showAddToFavoriteDialog(requireContext())
                is ActionFavoriteRemoved -> customDialog.showRemoveFromFavoriteDialog(requireContext())
            }
        }
    }

    private fun viewFullReview(review: Review) {
        val i = Intent(activity, WebViewActivity::class.java).apply {
            putExtra("url", review.url)
            putExtra("title", "Review Detail by ${review.author}")
        }
        startActivity(i)
    }
}