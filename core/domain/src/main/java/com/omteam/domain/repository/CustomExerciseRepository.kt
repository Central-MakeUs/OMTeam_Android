package com.omteam.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * 커스텀 운동 Repository
 */
interface CustomExerciseRepository {
    
    /**
     * 모든 커스텀 운동 이름 조회
     */
    fun getAllCustomExercises(): Flow<List<String>>
    
    /**
     * 커스텀 운동 추가
     * 
     * @param name 운동 이름
     * @return 추가된 운동의 ID
     */
    suspend fun addCustomExercise(name: String): Long
    
    /**
     * 커스텀 운동 삭제
     * 
     * @param name 삭제할 운동 이름
     */
    suspend fun deleteCustomExercise(name: String)
    
    /**
     * 모든 커스텀 운동 삭제
     */
    suspend fun deleteAllCustomExercises()
}