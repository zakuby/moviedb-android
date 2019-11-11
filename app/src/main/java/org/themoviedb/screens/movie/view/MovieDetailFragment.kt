package org.themoviedb.screens.movie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.MainActivity
import org.themoviedb.databinding.FragmentDetailMovieBinding
import org.themoviedb.models.Movie
import org.themoviedb.screens.movie.viewmodel.MovieDetailViewModel
import org.themoviedb.utils.ext.observe
import java.lang.Exception
import javax.inject.Inject

class MovieDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MovieDetailViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentDetailMovieBinding

    private val adapter by lazy { MovieCastsAdapter() }

    private val movieArgs: MovieDetailFragmentArgs by navArgs()

    private val movie: Movie by lazy { movieArgs.movie }

    private val activity: MainActivity by lazy { getActivity() as MainActivity }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailMovieBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            movie = this@MovieDetailFragment.movie
            activity.supportActionBar?.title = this@MovieDetailFragment.movie.title
            viewModel = this@MovieDetailFragment.viewModel
            recyclerViewCast.apply {
                layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@MovieDetailFragment.adapter
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        activity.supportActionBar?.show()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.fetchMovieCasts(movie.id ?: "550")

        observe(viewModel.getMovieCasts()){ casts ->
            adapter.loadCasts(casts)
        }
    }

}