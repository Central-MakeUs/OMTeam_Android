package com.omteam.data.di

import com.omteam.domain.repository.AuthRepository
import com.omteam.domain.usecase.GetUserInfoUseCase
import com.omteam.domain.usecase.LogoutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(
        authRepository: AuthRepository
    ): GetUserInfoUseCase = GetUserInfoUseCase(authRepository)

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        authRepository: AuthRepository
    ): LogoutUseCase = LogoutUseCase(authRepository)
}
