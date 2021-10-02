package com.amirhparhizgar.revision.service.setting

import kotlinx.coroutines.flow.Flow

interface Setting<T> {
    suspend fun set(value: T)
    val flow: Flow<T>
}