package com.farpost.mobile.di

import com.farpost.mobile.data.auth.AuthRepository
import com.farpost.mobile.data.auth.AuthRepositoryImpl
import com.farpost.mobile.data.scout.ScoutRepository
import com.farpost.mobile.data.scout.ScoutRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    abstract fun bindScoutRepository(impl: ScoutRepositoryImpl): ScoutRepository
}
