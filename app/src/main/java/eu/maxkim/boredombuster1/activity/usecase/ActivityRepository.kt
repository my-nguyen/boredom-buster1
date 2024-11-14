package eu.maxkim.boredombuster1.activity.usecase

import androidx.lifecycle.LiveData
import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.model.Result

interface ActivityRepository {
    suspend fun getNewActivity(): Result<Activity>

    suspend fun saveActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    suspend fun isActivitySaved(key: String): Boolean

    fun getActivityListLiveData(): LiveData<List<Activity>>
}