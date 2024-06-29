package com.app.newsviewr.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.app.newsviewr.network.ApiService
import com.app.newsviewr.repository.MainRepository
import com.app.newsviewr.ui.viewmodelfactory.NewsViewModelProviderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiService.create()
    }

    @Singleton
    @Provides
    fun provideMainRepository(apiService: ApiService): MainRepository {
        return MainRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(app: Application, mainRepository: MainRepository): ViewModelProvider.Factory {
        return NewsViewModelProviderFactory(app, mainRepository)
    }
}