package com.amirhparhizgar.revision.service.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class SimpleSetting<T>(private val dataStore: DataStore<Preferences>) : Setting<T> {
    protected abstract val key: Preferences.Key<T>
    protected abstract val default: T
    override suspend fun set(value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    override val flow: Flow<T> = dataStore.data.map { it[key] ?: default }
}