package eu.maxkim.boredombuster1.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.maxkim.boredombuster1.activity.framework.db.ActivityDao
import eu.maxkim.boredombuster1.framework.AppDatabase
import javax.inject.Singleton

// specify which component to install this module, and which module to replace
// this replaces DbModule containing AppDatabase and ActivityDao bindings
@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DbModule::class])
object TestDbModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): AppDatabase {
        // create an in-memory Room database
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideActivityDao(appDatabase: AppDatabase): ActivityDao {
        return appDatabase.activityDao()
    }
}
