package org.themoviedb.core.base

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.themoviedb.core.dagger.component.DaggerAppComponent

class BaseApplication : DaggerApplication() {
    private val appComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent.inject(this)
        return appComponent
    }
}