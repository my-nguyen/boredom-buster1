package eu.maxkim.boredombuster1.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.maxkim.boredombuster1.activity.usecase.DeleteActivity
import eu.maxkim.boredombuster1.activity.usecase.DeleteActivityImpl
import eu.maxkim.boredombuster1.activity.usecase.GetFavoriteActivities
import eu.maxkim.boredombuster1.activity.usecase.GetFavoriteActivitiesImpl
import eu.maxkim.boredombuster1.activity.usecase.GetRandomActivity
import eu.maxkim.boredombuster1.activity.usecase.GetRandomActivityImpl
import eu.maxkim.boredombuster1.activity.usecase.IsActivitySaved
import eu.maxkim.boredombuster1.activity.usecase.IsActivitySavedImpl
import eu.maxkim.boredombuster1.activity.usecase.SaveActivity
import eu.maxkim.boredombuster1.activity.usecase.SaveActivityImpl

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetRandomActivity(impl: GetRandomActivityImpl): GetRandomActivity

    @Binds
    abstract fun bindSaveActivity(impl: SaveActivityImpl): SaveActivity

    @Binds
    abstract fun bindDeleteActivity(impl: DeleteActivityImpl): DeleteActivity

    @Binds
    abstract fun bindIsActivitySaved(impl: IsActivitySavedImpl): IsActivitySaved

    @Binds
    abstract fun bindGetFavoriteActivities(impl: GetFavoriteActivitiesImpl): GetFavoriteActivities
}