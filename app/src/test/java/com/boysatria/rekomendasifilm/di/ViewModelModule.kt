package com.boysatria.rekomendasifilm.di

import com.boysatria.rekomendasifilm.ui.favorite.FavoriteViewModel
import com.boysatria.rekomendasifilm.ui.movies.MoviesViewModel
import com.boysatria.rekomendasifilm.ui.tv_series.TvSeriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//Module Koin untuk inisialisasi awal untuk view model
val viewModelModule = module {
    viewModel { MoviesViewModel(get(), get()) }
    viewModel { TvSeriesViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get()) }
}