package eu.maxkim.boredombuster1.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.maxkim.boredombuster1.activity.framework.datasource.ActivityLocalDataSourceImpl
import eu.maxkim.boredombuster1.activity.framework.datasource.ActivityRemoteDataSourceImpl
import eu.maxkim.boredombuster1.activity.repository.ActivityLocalDataSource
import eu.maxkim.boredombuster1.activity.repository.ActivityRemoteDataSource

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindActivityRemoteDataSource(impl: ActivityRemoteDataSourceImpl): ActivityRemoteDataSource

    @Binds
    abstract fun bindActivityLocalDataSource(impl: ActivityLocalDataSourceImpl): ActivityLocalDataSource
}