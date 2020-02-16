package org.themoviedb.favorite.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import org.themoviedb.favorite.R

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: FavoritePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBinding()
    }

    private fun initBinding() {
        pagerAdapter = FavoritePagerAdapter(supportFragmentManager, lifecycle)
        viewPager2.apply {
            offscreenPageLimit = 2
            adapter = pagerAdapter
        }
        TabLayoutMediator(tabLayout, viewPager2) { tab, pos ->
            tab.text = when (pos) {
                0 -> resources.getString(R.string.tab_movie_title)
                else -> resources.getString(R.string.tab_tv_show_title)
            }
        }.attach()
    }

    private inner class FavoritePagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> MovieFragment.newInstance()
            else -> TvShowFragment.newInstance()
        }
    }
}
