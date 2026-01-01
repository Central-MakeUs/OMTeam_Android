package com.omteam.data.di

import com.omteam.data.datasource.AuthDataSource
import com.omteam.data.datasource.KakaoAuthDataSource
import com.omteam.data.repository.AuthRepositoryImpl
import com.omteam.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        kakaoAuthDataSource: KakaoAuthDataSource
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}