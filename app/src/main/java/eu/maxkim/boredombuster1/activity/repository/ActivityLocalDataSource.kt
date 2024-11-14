package eu.maxkim.boredombuster1.activity.repository

import androidx.lifecycle.LiveData
import eu.maxkim.boredombuster1.activity.model.Activity

interface ActivityLocalDataSource {
    suspend fun saveActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    suspend fun isActivitySaved(key: String): Boolean

    fun getActivityListLiveData(): LiveData<List<Activity>>
}