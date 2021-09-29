package com.amirhparhizgar.revision.service.data_source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.TaskOldness
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Before
import org.junit.Test

class RepositoryImplTest {
    private lateinit var repository: Repository
    private lateinit var db: TaskDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, TaskDatabase::class.java
        ).build()
        repository = RepositoryImpl(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAllTasks() {
        val tasks = listOf(createDummyTask(1), createDummyTask(2))
        runBlocking {
            tasks.forEach {
                repository.saveTask(it)
            }
        }
        runBlocking {
            val receivedTasks = repository.getAllTasks().first()
            assertThat(receivedTasks).containsAnyIn(tasks)
        }
    }

    @Test
    fun getTask() {
        runBlocking {
            repository.saveTask(createDummyTask(id = 2))
        }
        runBlocking {
            val task = repository.getTask(2)
            assertThat(task.id).isEqualTo(2)
        }
    }

    @Test
    fun getIntervalCount() {
        runBlocking {
            repeat(1) {
                repository.saveTask(createDummyTask(interval = TaskOldness.Unseen.range.first))
            }
            repeat(2) {
                repository.saveTask(createDummyTask(interval = TaskOldness.Learning.range.first))
            }
            repeat(4) {
                repository.saveTask(createDummyTask(interval = TaskOldness.Young.range.first))
            }
            repeat(8) {
                repository.saveTask(createDummyTask(interval = TaskOldness.Mature.range.first))
            }
        }
        assertThat(repository.getOldnessCount(TaskOldness.Unseen)).isEqualTo(1)
        assertThat(repository.getOldnessCount(TaskOldness.Learning)).isEqualTo(2)
        assertThat(repository.getOldnessCount(TaskOldness.Young)).isEqualTo(4)
        assertThat(repository.getOldnessCount(TaskOldness.Mature)).isEqualTo(8)
    }

    @Test
    fun getProjectsStartWith() {
        runBlocking {
            repository.saveTask(createDummyTask(project = "apple"))
            repository.saveTask(createDummyTask(project = "apple"))
            repository.saveTask(createDummyTask(project = "orange"))
        }
        runBlocking {
            val startWithA = repository.getProjectsStartWith("a")
            assertThat(startWithA).containsExactly("apple")

            val startWithNothing = repository.getProjectsStartWith("")
            assertThat(startWithNothing).containsExactly("apple", "orange")
        }
    }

    private fun createDummyTask(
        id: Int? = null,
        name: String = "dummy name",
        project: String = "dummy project",
        interval: Int = 0
    ): Task {
        return if (id == null)
            Task(name = name, project = project, interval = interval)
        else
            Task(id = id, name = name, project = project, interval = interval)
    }
}