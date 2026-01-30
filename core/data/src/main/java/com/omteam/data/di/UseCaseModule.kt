package com.omteam.data.di

import com.omteam.domain.repository.AuthRepository
import com.omteam.domain.repository.CharacterRepository
import com.omteam.domain.repository.ReportRepository
import com.omteam.domain.usecase.CheckAutoLoginUseCase
import com.omteam.domain.usecase.GetCharacterInfoUseCase
import com.omteam.domain.usecase.GetOnboardingInfoUseCase
import com.omteam.domain.usecase.GetUserInfoUseCase
import com.omteam.domain.usecase.GetWeeklyReportUseCase
import com.omteam.domain.usecase.LogoutUseCase
import com.omteam.domain.usecase.LoginWithIdTokenUseCase
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
    
    @Provides
    @Singleton
    fun provideLoginWithIdTokenUseCase(
        authRepository: AuthRepository
    ): LoginWithIdTokenUseCase = LoginWithIdTokenUseCase(authRepository)
    
    @Provides
    @Singleton
    fun provideGetOnboardingInfoUseCase(
        authRepository: AuthRepository
    ): GetOnboardingInfoUseCase = GetOnboardingInfoUseCase(authRepository)
    
    @Provides
    @Singleton
    fun provideCheckAutoLoginUseCase(
        authRepository: AuthRepository
    ): CheckAutoLoginUseCase = CheckAutoLoginUseCase(authRepository)
    
    @Provides
    @Singleton
    fun provideGetCharacterInfoUseCase(
        characterRepository: CharacterRepository
    ): GetCharacterInfoUseCase = GetCharacterInfoUseCase(characterRepository)
    
    @Provides
    @Singleton
    fun provideGetWeeklyReportUseCase(
        reportRepository: ReportRepository
    ): GetWeeklyReportUseCase = GetWeeklyReportUseCase(reportRepository)
}