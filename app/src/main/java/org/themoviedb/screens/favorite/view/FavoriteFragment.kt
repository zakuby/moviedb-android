package org.themoviedb.screens.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import org.themoviedb.R
import org.themoviedb.data.models.Movie
import org.themoviedb.databinding.FragmentFavoriteBinding
import org.themoviedb.screens.main.view.BottomNavigationFragment

class FavoriteFragment : DaggerFragment() {

    private lateinit var pagerAdapter: FavoritePagerAdapter
    private lateinit var binding: FragmentFavoriteBinding

    private val parent by lazy { requireParentFragment().parentFragment as BottomNavigationFragment }

    fun navigateToDetail(movie: Movie) = parent.navigateToDetail(movie)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagerAdapter()
        initTabLayout()
    }

    private fun initPagerAdapter() {
        pagerAdapter = FavoritePagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.apply {
            offscreenPageLimit = 2
            adapter = pagerAdapter
        }
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = when (pos) {
                0 -> resources.getString(R.string.bottom_nav_movie_title)
                else -> resources.getString(R.string.bottom_nav_tv_show_title)
            }
        }.attach()
    }

    private inner class FavoritePagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> FavoriteMovieFragment.newInstance()
            else -> FavoriteTvShowFragment.newInstance()
        }
    }
}