package org.themoviedb.screens.movie.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.themoviedb.R
import org.themoviedb.core.base.BaseActivity
import org.themoviedb.databinding.ActivityDetailMovieBinding
import org.themoviedb.models.Movie
import org.themoviedb.screens.movie.viewmodel.MovieDetailViewModel
import org.themoviedb.utils.ext.observe
import org.themoviedb.utils.ext.setTransparentStatusBar
import javax.inject.Inject

class MovieDetailActivity : BaseActivity<ActivityDetailMovieBinding>(
    layoutRes = R.layout.activity_detail_movie,
    isWhiteStatusBar = false
) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MovieDetailViewModel by viewModels { viewModelFactory }


    private val adapter by lazy { MovieCastsAdapter() }

    private val movie: Movie by lazy { intent.getParcelableExtra("movie") as Movie }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MovieDetailActivity
            movie = this@MovieDetailActivity.movie
            viewModel = this@MovieDetailActivity.viewModel
            backButton.setOnClickListener { onBackPressed() }
            recyclerViewCast.apply {
                layoutManager = LinearLayoutManager(
                    this@MovieDetailActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = this@MovieDetailActivity.adapter
            }
        }
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.fetchMovieCasts(movie.id ?: return)

        observe(viewModel.getMovieCasts()) { casts ->
            adapter.loadCasts(casts)
        }
    }
}