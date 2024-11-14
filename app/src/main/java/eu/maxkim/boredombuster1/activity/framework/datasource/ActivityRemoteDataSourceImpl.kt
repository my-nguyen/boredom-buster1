package eu.maxkim.boredombuster1.activity.framework.datasource

import eu.maxkim.boredombuster1.activity.framework.api.ActivityApiClient
import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.repository.ActivityRemoteDataSource
import eu.maxkim.boredombuster1.model.Result
import eu.maxkim.boredombuster1.model.runCatching
import javax.inject.Inject

class ActivityRemoteDataSourceImpl @Inject constructor(
    private val activityApiClient: ActivityApiClient
) : ActivityRemoteDataSource {

    override suspend fun getActivity(): Result<Activity> {
        return runCatching {
            activityApiClient.getActivity()
        }
    }
}