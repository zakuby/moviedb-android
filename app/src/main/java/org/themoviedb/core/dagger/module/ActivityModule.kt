package org.themoviedb.core.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.themoviedb.MainActivity
import org.themoviedb.MainActivityModule
import org.themoviedb.core.dagger.ActivityScoped
import org.themoviedb.screens.WebViewActivity

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeWebViewActivity(): WebViewActivity
}