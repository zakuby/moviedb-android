package org.themoviedb

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.themoviedb.core.base.BaseActivity
import org.themoviedb.core.dagger.FragmentScoped
import org.themoviedb.databinding.ActivityMainBinding
import org.themoviedb.screens.movie.view.MovieDetailFragment
import org.themoviedb.screens.movie.view.MoviesFragment
import org.themoviedb.screens.profile.ProfileFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val navController by lazy { findNavController(R.id.nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity()
    }

    private fun setupActivity(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
    }

    private fun openProfile(){
        navController.navigate(R.id.action_to_profile)
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

@Module
abstract class MainActivityModule{
    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeMoviesFragment(): MoviesFragment


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeMovieDetailFragment(): MovieDetailFragment


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment
}
