package com.amirhparhizgar.revision.base_classes

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Before
import java.io.File

@ExperimentalCoroutinesApi
abstract class DataStoreTest : CoroutineTest() {

    private lateinit var preferencesScope: CoroutineScope
    protected lateinit var dataStore: DataStore<Preferences>
    abstract val dataStoreName: String
    private var dataStoreFile: File? = null

    @Before
    fun createDatastore() {
        preferencesScope = CoroutineScope(testDispatcher + Job())
        dataStoreFile = InstrumentationRegistry.getInstrumentation().targetContext
            .preferencesDataStoreFile("test-preferences-file")
        dataStore = PreferenceDataStoreFactory.create(scope = preferencesScope) {
            dataStoreFile!!
        }
    }

    @After
    fun removeDatastore() {
        dataStoreFile?.delete()

        preferencesScope.cancel()
    }
}