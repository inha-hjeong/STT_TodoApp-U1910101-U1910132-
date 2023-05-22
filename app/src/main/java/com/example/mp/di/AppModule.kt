package com.example.mp.di

import android.content.Context
import androidx.room.Room
import com.example.mp.data.AppRepository
import com.example.mp.data.local.AppDao
import com.example.mp.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "MPDatabase").build()

    @Provides
    @Singleton
    fun provideAppDao(appDatabase: AppDatabase): AppDao = appDatabase.appDao()

    @Provides
    @Singleton
    fun provideAppRepository(appDao: AppDao): AppRepository =
        AppRepository(appDao)
}