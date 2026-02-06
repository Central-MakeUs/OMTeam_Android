package com.omteam.database.datasource

import com.omteam.database.dao.CustomExerciseDao
import com.omteam.database.entity.CustomExerciseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 커스텀 운동 로컬 데이터 소스 구현체
 */
class CustomExerciseLocalDataSourceImpl @Inject constructor(
    private val customExerciseDao: CustomExerciseDao
) : CustomExerciseLocalDataSource {
    
    override fun getAllCustomExercises(): Flow<List<CustomExerciseEntity>> =
        customExerciseDao.getAllCustomExercises()
    
    override suspend fun getCustomExerciseById(id: Long): CustomExerciseEntity? =
        customExerciseDao.getCustomExerciseById(id)
    
    override suspend fun getCustomExerciseByName(name: String): CustomExerciseEntity? =
        customExerciseDao.getCustomExerciseByName(name)
    
    override suspend fun insertCustomExercise(name: String): Long =
        customExerciseDao.insertCustomExercise(
            CustomExerciseEntity(name = name)
        )
    
    override suspend fun updateCustomExercise(exercise: CustomExerciseEntity) =
        customExerciseDao.updateCustomExercise(exercise)
    
    override suspend fun deleteCustomExercise(exercise: CustomExerciseEntity) =
        customExerciseDao.deleteCustomExercise(exercise)
    
    override suspend fun deleteAllCustomExercises() =
        customExerciseDao.deleteAllCustomExercises()
}
