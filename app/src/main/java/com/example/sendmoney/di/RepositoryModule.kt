package com.example.sendmoney.di

import android.content.Context
import com.example.sendmoney.data.repository.ServicesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideServicesRepository(@ApplicationContext context: Context): ServicesRepository {
        return ServicesRepository(context)
    }
}
