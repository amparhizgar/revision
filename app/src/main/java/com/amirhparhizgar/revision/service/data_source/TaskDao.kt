package com.amirhparhizgar.revision.service.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amirhparhizgar.revision.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id=:id")
    suspend fun getTask(id: Int): Task

    @Query("SELECT * FROM tasks WHERE  :minMillis <= nextRepetitionMillis AND nextRepetitionMillis < :maxMillis")
    fun getTaskBetweenTimes(minMillis: Long, maxMillis: Long): Flow<List<Task>>

    @Query("DELETE FROM tasks WHERE id == :id")
    fun deleteTask(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTask(task: Task)

    @Query("SELECT COUNT(*) FROM tasks WHERE :min <= interval AND interval <= :max")
    fun getIntervalCountInclusive(min: Int, max: Int): Int

    @Query("SELECT DISTINCT project FROM tasks WHERE project LIKE :pattern")
    fun getDistinctProjectsLike(pattern: String): List<String>

    @Query("SELECT DISTINCT project FROM tasks")
    fun getProjects(): Flow<List<String>>
}
