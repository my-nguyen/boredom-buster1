package eu.maxkim.boredombuster1.activity.framework.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.repository.ActivityLocalDataSource

class FakeActivityLocalDataSource : ActivityLocalDataSource {

    override suspend fun saveActivity(activity: Activity) {
        // Save
    }

    override suspend fun deleteActivity(activity: Activity) {
        // Delete
    }

    override suspend fun isActivitySaved(key: String): Boolean {
        return false
    }

    override fun getActivityListLiveData(): LiveData<List<Activity>> {
        return MutableLiveData()
    }
}
