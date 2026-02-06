package com.omteam.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omteam.database.dao.CustomExerciseDao
import com.omteam.database.entity.CustomExerciseEntity

/**
 * OMTeam Room Database
 */
@Database(
    entities = [CustomExerciseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class OMTeamDatabase : RoomDatabase() {
    abstract fun customExerciseDao(): CustomExerciseDao
}
