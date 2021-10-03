package com.amirhparhizgar.revision.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.data_source.RepositoryImpl
import com.amirhparhizgar.revision.service.data_source.TaskDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindRepository(repository: RepositoryImpl): Repository

    companion object {
        @Provides
        @Singleton
        fun provideTaskDatabase(app: Application): TaskDatabase {
            return Room.databaseBuilder(
                app,
                TaskDatabase::class.java,
                TaskDatabase.DATABASE_NAME
            ).build()
        }

        @Provides
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            val name = "SettingStore"
            val corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null
            val produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() }
            val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

            return PreferenceDataStoreFactory.create(
                corruptionHandler = corruptionHandler,
                migrations = produceMigrations(context),
                scope = scope
            ) {
                context.preferencesDataStoreFile(name)
            }
        }

    }
}