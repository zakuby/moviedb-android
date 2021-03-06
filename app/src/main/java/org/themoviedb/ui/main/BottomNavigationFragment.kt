package org.themoviedb.ui.main

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.themoviedb.R
import org.themoviedb.databinding.FragmentBottomNavigationBinding
import org.themoviedb.data.local.models.Movie
import org.themoviedb.ui.base.BaseFragment

class BottomNavigationFragment : BaseFragment<FragmentBottomNavigationBinding>(R.layout.fragment_bottom_navigation) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {

        val navController =
            Navigation.findNavController(requireActivity(), R.id.nav_host_bottom_fragment)
                .apply {
                    addOnDestinationChangedListener { _, destination, _ ->
                        (activity as MainActivity).supportActionBar?.title = destination.label
                    }
                }

        binding.bottomNavBar.apply {
            itemIconTintList = null
            setupWithNavController(navController)
        }
    }

    fun setBottomNavPosition(@IdRes id: Int) {
        binding.bottomNavBar.selectedItemId = id
    }

    fun navigateToDetail(movie: Movie, isMovie: Boolean = true) {
        val action =
            BottomNavigationFragmentDirections.actionBottomNavFragmentToDetailFragment(movie.id, isMovie)
        findNavController().navigate(action)
    }
}
