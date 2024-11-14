package eu.maxkim.boredombuster1.activity.repository

import androidx.lifecycle.LiveData
import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.usecase.ActivityRepository
import eu.maxkim.boredombuster1.di.annotation.AppScope
import eu.maxkim.boredombuster1.di.annotation.IODispatcher
import eu.maxkim.boredombuster1.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Since we are using Retrofit and Room, we don't need to explicitly change our
 * dispatcher to IO. Those libraries handle that automatically. I wanted, however, to
 * introduce a test scenario, when injected dispatcher is being used in a component.
 *
 * The same goes for the appScope. We don't need to launch , this
 * logic is just to illustrate how to test these scenarios.
 */
class ActivityRepositoryImpl @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteDataSource: ActivityRemoteDataSource,
    private val localDataSource: ActivityLocalDataSource
) : ActivityRepository {

    /**
     * We are switching dispatcher here for educational
     * testing purposes only
     */
    override suspend fun getNewActivity(): Result<Activity> {
        return withContext(ioDispatcher) {
            remoteDataSource.getActivity()
        }
    }

    override suspend fun saveActivity(activity: Activity) {
        localDataSource.saveActivity(activity)
    }

    override suspend fun deleteActivity(activity: Activity) {
        localDataSource.deleteActivity(activity)
    }

    override suspend fun isActivitySaved(key: String): Boolean {
        return localDataSource.isActivitySaved(key)
    }

    override fun getActivityListLiveData(): LiveData<List<Activity>> {
        return localDataSource.getActivityListLiveData()
    }

    /**
     * This is a dummy method, just to illustrate how to test
     * coroutines launched in an injected scope.
     */
    fun getNewActivityInANewCoroutine() {
        appScope.launch(ioDispatcher) {
            delay(1000)

            val activity = remoteDataSource.getActivity()
        }
    }
}