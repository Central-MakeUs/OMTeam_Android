package com.omteam.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 사용자 커스텀 운동 Entity
 */
@Entity(tableName = "custom_exercises")
data class CustomExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
