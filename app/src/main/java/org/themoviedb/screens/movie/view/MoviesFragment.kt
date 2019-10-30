package org.themoviedb.screens.movie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.databinding.FragmentMoviesBinding
import org.themoviedb.models.Movie
import org.themoviedb.screens.movie.viewmodel.MoviesViewModel
import javax.inject.Inject

class MoviesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoviesViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentMoviesBinding

    private val adapter by lazy { MoviesListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@MoviesFragment.viewModel
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = this@MoviesFragment.adapter
                }
            }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {

        val dummyMovies = listOf(
            Movie(
                1,
                "Spider-Man: Into the Spider-Verse",
                "December 14, 2018",
                "Miles Morales is juggling his life between being a high school student and being a spider-man. When Wilson Kingpin Fisk uses a super collider, others from across the Spider-Verse are transported to this dimension.",
                "9.0"
            ),
            Movie(2,
                "Joker Jared Leto",
                "October 4, 2019",
                "During the 1980s, a failed actor is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.",
                "5.1"),
            Movie(3,
                "Dragonball Evolution",
                "April 8, 2009",
                "The young warrior Son Goku sets out on a quest, racing against time and the vengeful King Piccolo, to collect a set of seven magical orbs that will grant their wielder unlimited power.",
                "2.5")
        )

        adapter.loadMovies(dummyMovies)
    }
}