package com.example.productsearch.di

import androidx.lifecycle.ViewModel
import com.example.productsearch.presentation.screen.detail.DetailViewModel
import com.example.productsearch.presentation.screen.main.MainViewModel
import com.example.productsearch.presentation.screen.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    @Binds
    fun bindDetailViewModel(viewModel: DetailViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    @Binds
    fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel
}