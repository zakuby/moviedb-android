package org.themoviedb.ui.base

import androidx.work.Configuration
import androidx.work.WorkManager
import com.crashlytics.android.Crashlytics
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import org.themoviedb.di.component.DaggerAppComponent
import org.themoviedb.utils.DaggerAwareWorkerFactory
import javax.inject.Inject

class BaseApplication : DaggerApplication() {

    @Inject
    lateinit var daggerAwareWorkerFactory: DaggerAwareWorkerFactory

    private val appComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent.inject(this)
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        setupWorkManager()
    }

    private fun setupWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(daggerAwareWorkerFactory)
            .build()
        WorkManager.initialize(this, config)
    }
}