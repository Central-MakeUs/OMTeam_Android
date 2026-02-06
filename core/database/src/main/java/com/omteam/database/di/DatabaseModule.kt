package com.omteam.database.di

import android.content.Context
import androidx.room.Room
import com.omteam.database.OMTeamDatabase
import com.omteam.database.dao.CustomExerciseDao
import com.omteam.database.datasource.CustomExerciseLocalDataSource
import com.omteam.database.datasource.CustomExerciseLocalDataSourceImpl
import dagger.Binds
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
    fun provideOMTeamDatabase(
        @ApplicationContext context: Context
    ): OMTeamDatabase = Room.databaseBuilder(
        context,
        OMTeamDatabase::class.java,
        "omteam_database"
    ).build()
    
    @Provides
    fun provideCustomExerciseDao(database: OMTeamDatabase): CustomExerciseDao =
        database.customExerciseDao()
}

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseBindModule {
    
    @Binds
    fun bindCustomExerciseLocalDataSource(
        impl: CustomExerciseLocalDataSourceImpl
    ): CustomExerciseLocalDataSource
}
