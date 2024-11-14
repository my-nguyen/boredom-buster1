package eu.maxkim.boredombuster1.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.maxkim.boredombuster1.activity.repository.ActivityRepositoryImpl
import eu.maxkim.boredombuster1.activity.usecase.ActivityRepository

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindActivityRepository(impl: ActivityRepositoryImpl): ActivityRepository
}