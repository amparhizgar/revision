package com.amirhparhizgar.revision.di

import android.app.Application
import androidx.room.Room
import com.amirhparhizgar.revision.service.data_source.Repository
import com.amirhparhizgar.revision.service.data_source.RepositoryImpl
import com.amirhparhizgar.revision.service.data_source.TaskDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    }
}