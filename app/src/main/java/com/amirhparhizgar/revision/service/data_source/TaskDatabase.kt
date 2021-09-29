package com.amirhparhizgar.revision.service.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amirhparhizgar.revision.model.Task

@Database(
    entities = [Task::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}