package org.themoviedb.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.themoviedb.data.local.room.Database
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): Database =
        Room.databaseBuilder(application, Database::class.java, "TheMovieDb.db").build()
}