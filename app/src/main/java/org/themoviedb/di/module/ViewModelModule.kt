package org.themoviedb.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.themoviedb.di.ViewModelKey
import org.themoviedb.ui.favorite.viewmodel.FavoriteMovieViewModel
import org.themoviedb.ui.favorite.viewmodel.FavoriteTvShowViewModel
import org.themoviedb.ui.detail.DetailViewModel
import org.themoviedb.ui.movie.MoviesViewModel
import org.themoviedb.ui.profile.ProfileViewModel
import org.themoviedb.ui.tvshow.TvShowViewModel
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
    @ViewModelKey(TvShowViewModel::class)
    internal abstract fun tvShowViewModel(viewModel: TvShowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun movieDetailViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMovieViewModel::class)
    internal abstract fun favoriteMovieViewModel(viewModel: FavoriteMovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteTvShowViewModel::class)
    internal abstract fun favoriteTvShowViewModel(viewModel: FavoriteTvShowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel
}