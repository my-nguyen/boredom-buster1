package eu.maxkim.boredombuster1.activity.framework.datasource

import androidx.lifecycle.LiveData
import eu.maxkim.boredombuster1.activity.framework.db.ActivityDao
import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.repository.ActivityLocalDataSource
import javax.inject.Inject

class ActivityLocalDataSourceImpl @Inject constructor(
    private val activityDao: ActivityDao
) : ActivityLocalDataSource {

    override suspend fun saveActivity(activity: Activity) {
        activityDao.insert(activity)
    }

    override suspend fun deleteActivity(activity: Activity) {
        activityDao.delete(activity)
    }

    override suspend fun isActivitySaved(key: String): Boolean {
        return activityDao.isActivitySaved(key)
    }

    override fun getActivityListLiveData(): LiveData<List<Activity>> {
        return activityDao.getAll()
    }
}