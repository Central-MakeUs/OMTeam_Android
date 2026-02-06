package com.omteam.data.di

import com.omteam.domain.repository.*
import com.omteam.domain.usecase.*
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
        onboardingRepository: OnboardingRepository
    ): GetOnboardingInfoUseCase = GetOnboardingInfoUseCase(onboardingRepository)
    
    @Provides
    @Singleton
    fun provideCheckAutoLoginUseCase(
        authRepository: AuthRepository,
        onboardingRepository: OnboardingRepository
    ): CheckAutoLoginUseCase = CheckAutoLoginUseCase(authRepository, onboardingRepository)
    
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
    
    @Provides
    @Singleton
    fun provideGetDailyRecommendedMissionsUseCase(
        missionRepository: MissionRepository
    ): GetDailyRecommendedMissionsUseCase = GetDailyRecommendedMissionsUseCase(missionRepository)

    @Provides
    @Singleton
    fun provideUpdateLifestyleUseCase(
        onboardingRepository: OnboardingRepository
    ): UpdateLifestyleUseCase = UpdateLifestyleUseCase(onboardingRepository)
    
    @Provides
    @Singleton
    fun provideUpdatePreferredExerciseUseCase(
        onboardingRepository: OnboardingRepository
    ): UpdatePreferredExerciseUseCase = UpdatePreferredExerciseUseCase(onboardingRepository)
    
    @Provides
    @Singleton
    fun provideUpdateMinExerciseMinutesUseCase(
        onboardingRepository: OnboardingRepository
    ): UpdateMinExerciseMinutesUseCase = UpdateMinExerciseMinutesUseCase(onboardingRepository)
    
    @Provides
    @Singleton
    fun provideUpdateAvailableTimeUseCase(
        onboardingRepository: OnboardingRepository
    ): UpdateAvailableTimeUseCase = UpdateAvailableTimeUseCase(onboardingRepository)
}