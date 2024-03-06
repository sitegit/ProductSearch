package com.example.productsearch.di

import com.example.productsearch.data.ApiFactory
import com.example.productsearch.data.ApiService
import com.example.productsearch.data.ProductsRepositoryImpl
import com.example.productsearch.domain.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindProductsRepository(repositoryImpl: ProductsRepositoryImpl): ProductsRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}