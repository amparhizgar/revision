package com.amirhparhizgar.revision.service.setting

import com.amirhparhizgar.revision.base_classes.DataStoreTest
import com.amirhparhizgar.revision.model.ThemeMode
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingStoreTest : DataStoreTest() {
    override val dataStoreName: String
        get() = "settingStore test"

    lateinit var settingStore: SettingStore

    @Before
    fun initSettingStore() {
        settingStore = SettingStore(dataStore)
    }

    @Test
    fun getTodoRemindingDefault() {
        runBlocking {
            assertThat(settingStore.todoReminding.flow.first()).isEqualTo(settingStore.TodoReminding().default)
        }
    }

    @Test
    fun setTodoReminding() {
        runBlocking {
            val value = settingStore.TodoReminding().default.not()
            settingStore.todoReminding.set(value)
            assertThat(settingStore.todoReminding.flow.first()).isEqualTo(value)
        }
    }

    @Test
    fun setThemeSetting() {
        runBlocking {
            val value = ThemeMode.DARK
            settingStore.themeSetting.set(value)
            assertThat(settingStore.themeSetting.flow.first()).isEqualTo(value)
        }
    }
}