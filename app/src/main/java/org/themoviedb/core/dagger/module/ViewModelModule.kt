package org.themoviedb.core.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.themoviedb.core.dagger.ViewModelKey
import org.themoviedb.screens.movie.viewmodel.MovieDetailViewModel
import org.themoviedb.screens.movie.viewmodel.MoviesViewModel
import org.themoviedb.screens.profile.ProfileViewModel
import org.themoviedb.utils.ViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    internal abstract fun moviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    internal abstract fun movieDetailViewModel(viewModel: MovieDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel
}