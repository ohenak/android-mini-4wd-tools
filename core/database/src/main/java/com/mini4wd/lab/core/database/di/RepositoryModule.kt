package com.mini4wd.lab.core.database.di

import com.mini4wd.lab.core.database.repository.CarProfileRepository
import com.mini4wd.lab.core.database.repository.CarProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarProfileRepository(
        impl: CarProfileRepositoryImpl
    ): CarProfileRepository
}
