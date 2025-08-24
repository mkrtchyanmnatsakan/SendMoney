package com.example.sendmoney.di

import android.content.Context
import androidx.room.Room
import com.example.sendmoney.db.AppDatabase
import com.example.sendmoney.db.RequestDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "send_money_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRequestDao(appDatabase: AppDatabase): RequestDao {
        return appDatabase.requestDao()
    }
}
