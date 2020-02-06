package org.themoviedb.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.themoviedb.R
import org.themoviedb.databinding.ActivityMainBinding
import org.themoviedb.di.FragmentScoped
import org.themoviedb.ui.base.BaseActivity
import org.themoviedb.ui.detail.DetailFragment
import org.themoviedb.ui.favorite.view.FavoriteFragment
import org.themoviedb.ui.favorite.view.FavoriteMovieFragment
import org.themoviedb.ui.favorite.view.FavoriteTvShowFragment
import org.themoviedb.ui.movie.MoviesFragment
import org.themoviedb.ui.profile.ProfileFragment
import org.themoviedb.ui.tvshow.TvShowFragment
import org.themoviedb.utils.ext.setBlackStatusBar
import org.themoviedb.utils.ext.setWhiteStatusBar

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val navController by lazy { findNavController(R.id.nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity()
    }

    private fun setupActivity() {
        val toolbar = binding.toolbar

        setSupportActionBar(toolbar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detail_fragment -> setBlackStatusBar()
                else -> setWhiteStatusBar()
            }
        }

        setupActionBarWithNavController(navController)
    }

    private fun openProfile() {
        navController.navigate(R.id.action_to_profile)
    }

    private fun openSettings() {
        val settingsIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(settingsIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                openProfile()
                true
            }
            R.id.action_settings -> {
                openSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

@Module
abstract class MainActivityModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeBottomNavFragment(): BottomNavigationFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeMoviesFragment(): MoviesFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeTvShowFragment(): TvShowFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeFavoriteMovieFragment(): FavoriteMovieFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeFavoriteTvShowFragment(): FavoriteTvShowFragment
}
