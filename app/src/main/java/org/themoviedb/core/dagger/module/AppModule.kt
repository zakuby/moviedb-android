package org.themoviedb.core.dagger.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideResources(context: Context): Resources = context.resources
    }
}