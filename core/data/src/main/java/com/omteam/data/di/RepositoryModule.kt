package com.omteam.data.di

import com.omteam.data.repository.AuthRepositoryImpl
import com.omteam.data.repository.CharacterRepositoryImpl
import com.omteam.data.repository.MissionRepositoryImpl
import com.omteam.domain.repository.AuthRepository
import com.omteam.domain.repository.CharacterRepository
import com.omteam.domain.repository.MissionRepository
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindMissionRepository(
        missionRepositoryImpl: MissionRepositoryImpl
    ): MissionRepository
    
    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository
}