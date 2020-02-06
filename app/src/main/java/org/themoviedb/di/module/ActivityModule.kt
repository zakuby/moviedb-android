package org.themoviedb.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.themoviedb.ui.main.MainActivity
import org.themoviedb.ui.main.MainActivityModule
import org.themoviedb.di.ActivityScoped
import org.themoviedb.ui.main.WebViewActivity

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeWebViewActivity(): WebViewActivity
}