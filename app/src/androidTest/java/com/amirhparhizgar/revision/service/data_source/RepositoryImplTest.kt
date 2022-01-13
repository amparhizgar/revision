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
                repository.saveTask(createDummyTask(interval = TaskOldness.UNSEEN.range.first))
                repository.saveTask(createDummyTask(interval = TaskOldness.UNSEEN.range.last))
            }
            repeat(2) {
                repository.saveTask(createDummyTask(interval = TaskOldness.LEARNING.range.first))
                repository.saveTask(createDummyTask(interval = TaskOldness.LEARNING.range.last))
            }
            repeat(4) {
                repository.saveTask(createDummyTask(interval = TaskOldness.YOUNG.range.first))
                repository.saveTask(createDummyTask(interval = TaskOldness.YOUNG.range.last))
            }
            repeat(8) {
                repository.saveTask(createDummyTask(interval = TaskOldness.MATURE.range.first))
                repository.saveTask(createDummyTask(interval = TaskOldness.MATURE.range.last))
            }
        }
        assertThat(repository.getOldnessCount(TaskOldness.UNSEEN)).isEqualTo(2)
        assertThat(repository.getOldnessCount(TaskOldness.LEARNING)).isEqualTo(4)
        assertThat(repository.getOldnessCount(TaskOldness.YOUNG)).isEqualTo(8)
        assertThat(repository.getOldnessCount(TaskOldness.MATURE)).isEqualTo(16)
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

    @Test
    fun getTasksForToday() {
        runBlocking {
            repository.saveTask(
                Task(
                    name = "today", project = "project",
                    nextRepetitionMillis = System.currentTimeMillis()
                )
            )
            repository.saveTask(
                Task(
                    name = "tomorrow", project = "project",
                    nextRepetitionMillis = System.currentTimeMillis() + oneDayMillis
                )
            )
            repository.saveTask(
                Task(
                    name = "yesterday", project = "project",
                    nextRepetitionMillis = System.currentTimeMillis() - oneDayMillis
                )
            )
        }
        runBlocking {
            val todayTasks = repository.getTodoTasks().first()
            assertThat(todayTasks.map { it.name }).containsExactly("yesterday", "today")
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

    companion object {
        const val oneDayMillis = 24 * 60 * 60 * 1000
    }
}