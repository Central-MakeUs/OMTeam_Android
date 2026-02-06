package com.omteam.data.repository

import com.omteam.database.datasource.CustomExerciseLocalDataSource
import com.omteam.domain.repository.CustomExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 커스텀 운동 Repository 구현체
 */
class CustomExerciseRepositoryImpl @Inject constructor(
    private val localDataSource: CustomExerciseLocalDataSource
) : CustomExerciseRepository {
    
    override fun getAllCustomExercises(): Flow<List<String>> =
        localDataSource.getAllCustomExercises().map { entities ->
            entities.map { it.name }
        }
    
    override suspend fun addCustomExercise(name: String): Long =
        localDataSource.insertCustomExercise(name)
    
    override suspend fun deleteCustomExercise(name: String) {
        val exercise = localDataSource.getCustomExerciseByName(name)
        exercise?.let { localDataSource.deleteCustomExercise(it) }
    }
    
    override suspend fun deleteAllCustomExercises() =
        localDataSource.deleteAllCustomExercises()
}
