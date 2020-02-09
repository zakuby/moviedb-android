package org.themoviedb.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import org.themoviedb.data.local.room.FavoriteDatabase
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): FavoriteDatabase = FavoriteDatabase.getInstance(application)
}