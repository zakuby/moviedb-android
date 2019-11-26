package org.themoviedb.core.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.themoviedb.screens.main.view.MainActivity
import org.themoviedb.screens.main.view.MainActivityModule
import org.themoviedb.core.dagger.ActivityScoped
import org.themoviedb.screens.main.view.WebViewActivity
import org.themoviedb.screens.main.view.DetailActivity

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeMovieDetailActivity(): DetailActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeWebViewActivity(): WebViewActivity
}