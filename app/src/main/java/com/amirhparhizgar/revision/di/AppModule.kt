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
import com.amirhparhizgar.revision.service.human_readable_date.DateTranslatableStrings
import com.amirhparhizgar.revision.service.human_readable_date.DateTranslatableStringsImpl
import com.amirhparhizgar.revision.service.scheduler.Scheduler
import com.amirhparhizgar.revision.service.scheduler.SchedulerImpl
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
    @Singleton
    abstract fun bindRepository(repository: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun bindDateTranslatableStrings(impl: DateTranslatableStringsImpl): DateTranslatableStrings

    @Binds
    @Singleton
    abstract fun bindScheduler(impl: SchedulerImpl): Scheduler

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
        @Singleton
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