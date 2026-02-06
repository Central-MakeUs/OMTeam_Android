package com.omteam.database.datasource

import com.omteam.database.entity.CustomExerciseEntity
import kotlinx.coroutines.flow.Flow

/**
 * 커스텀 운동 로컬 데이터 소스 인터페이스
 */
interface CustomExerciseLocalDataSource {
    
    /**
     * 모든 커스텀 운동 조회
     */
    fun getAllCustomExercises(): Flow<List<CustomExerciseEntity>>
    
    /**
     * 특정 커스텀 운동 조회
     */
    suspend fun getCustomExerciseById(id: Long): CustomExerciseEntity?
    
    /**
     * 이름으로 커스텀 운동 조회
     */
    suspend fun getCustomExerciseByName(name: String): CustomExerciseEntity?
    
    /**
     * 커스텀 운동 추가
     */
    suspend fun insertCustomExercise(name: String): Long
    
    /**
     * 커스텀 운동 수정
     */
    suspend fun updateCustomExercise(exercise: CustomExerciseEntity)
    
    /**
     * 커스텀 운동 삭제
     */
    suspend fun deleteCustomExercise(exercise: CustomExerciseEntity)
    
    /**
     * 모든 커스텀 운동 삭제
     */
    suspend fun deleteAllCustomExercises()
}
