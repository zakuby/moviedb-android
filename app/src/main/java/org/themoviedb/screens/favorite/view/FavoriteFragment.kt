package org.themoviedb.screens.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import org.themoviedb.R
import org.themoviedb.databinding.FragmentFavoriteBinding

class FavoriteFragment : DaggerFragment() {

    private val pagerAdapter: FavoritePagerAdapter by lazy { FavoritePagerAdapter(this) }

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = when (pos) {
                0 -> resources.getString(R.string.bottom_nav_movie_title)
                else -> resources.getString(R.string.bottom_nav_tv_show_title)
            }
        }.attach()
    }

    inner class FavoritePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FavoriteMovieFragment()
                else -> FavoriteTvShowFragment()
            }
        }

    }
}