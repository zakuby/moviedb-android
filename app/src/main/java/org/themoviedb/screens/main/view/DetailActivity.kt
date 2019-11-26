package org.themoviedb.screens.main.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.themoviedb.R
import org.themoviedb.core.base.BaseActivity
import org.themoviedb.databinding.ActivityDetailBinding
import org.themoviedb.models.Movie
import org.themoviedb.screens.movie.view.MovieCastsAdapter
import org.themoviedb.screens.main.viewmodel.DetailViewModel
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class DetailActivity : BaseActivity<ActivityDetailBinding>(
    layoutRes = R.layout.activity_detail,
    isWhiteStatusBar = false
) {

    companion object {
        const val EXTRA_DETAIL = "detail"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DetailViewModel by viewModels { viewModelFactory }

    private val adapter by lazy { MovieCastsAdapter() }

    private val movie: Movie by lazy { intent.getParcelableExtra(EXTRA_DETAIL) as Movie }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@DetailActivity
            movie = this@DetailActivity.movie
            viewModel = this@DetailActivity.viewModel
            backButton.setOnClickListener { onBackPressed() }
            recyclerViewCast.apply {
                layoutManager = LinearLayoutManager(
                    this@DetailActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = this@DetailActivity.adapter
            }
        }
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.fetchMovieCasts(movie.id ?: return, movie.isMovie)

        observe(viewModel.getLoading()) { isLoading ->
            if (isLoading) {
                binding.apply {
                    castView.visibility = View.GONE
                    shimmerView.visibility = View.VISIBLE
                    shimmerView.startShimmer()
                }
            } else {
                binding.apply {
                    shimmerView.stopShimmer()
                    shimmerView.visibility = View.GONE
                    castView.visibility = View.VISIBLE
                }
            }
        }

        observe(viewModel.getMovieCasts()) { casts ->
            adapter.loadCasts(casts)
        }
    }
}