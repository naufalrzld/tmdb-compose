package com.example.moviedb.di

import com.example.moviedb.ui.detail.MovieDetailViewModel
import com.example.moviedb.ui.list.MoviesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MoviesViewModel(get(), get()) }
    viewModel { MovieDetailViewModel(get(), get()) }
}