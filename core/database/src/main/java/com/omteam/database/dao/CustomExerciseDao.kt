package com.omteam.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.omteam.database.entity.CustomExerciseEntity
import kotlinx.coroutines.flow.Flow

/**
 * 커스텀 운동 DAO
 */
@Dao
interface CustomExerciseDao {
    
    /**
     * 추가한 순서대로 모든 커스텀 운동 조회
     */
    @Query("SELECT * FROM custom_exercises ORDER BY createdAt ASC")
    fun getAllCustomExercises(): Flow<List<CustomExerciseEntity>>
    
    /**
     * 특정 커스텀 운동 조회
     */
    @Query("SELECT * FROM custom_exercises WHERE id = :id")
    suspend fun getCustomExerciseById(id: Long): CustomExerciseEntity?
    
    /**
     * 이름으로 커스텀 운동 조회
     */
    @Query("SELECT * FROM custom_exercises WHERE name = :name")
    suspend fun getCustomExerciseByName(name: String): CustomExerciseEntity?
    
    /**
     * 커스텀 운동 추가
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomExercise(exercise: CustomExerciseEntity): Long
    
    /**
     * 커스텀 운동 수정
     */
    @Update
    suspend fun updateCustomExercise(exercise: CustomExerciseEntity)
    
    /**
     * 커스텀 운동 삭제
     */
    @Delete
    suspend fun deleteCustomExercise(exercise: CustomExerciseEntity)
    
    /**
     * 모든 커스텀 운동 삭제
     */
    @Query("DELETE FROM custom_exercises")
    suspend fun deleteAllCustomExercises()
}