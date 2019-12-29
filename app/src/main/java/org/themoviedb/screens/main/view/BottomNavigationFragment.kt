package org.themoviedb.screens.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerFragment
import org.themoviedb.R
import org.themoviedb.databinding.FragmentBottomNavigationBinding
import org.themoviedb.data.models.Movie

class BottomNavigationFragment : DaggerFragment() {

    private lateinit var binding: FragmentBottomNavigationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomNavigationBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {

        val navController =
            Navigation.findNavController(requireActivity(),
                R.id.nav_host_bottom_fragment
            ).apply {
                addOnDestinationChangedListener { _, destination, _ ->
                    (activity as MainActivity).supportActionBar?.title = destination.label
                }
            }

        binding.bottomNavBar.apply {
            itemIconTintList = null
            setupWithNavController(navController)
        }
    }

    fun navigateToDetail(movie: Movie) {
        val action = BottomNavigationFragmentDirections.actionBottomNavFragmentToDetailFragment(movie)
        val activity = activity as MainActivity
        activity.supportActionBar?.hide()
        findNavController().navigate(action)
    }
}
