package eu.maxkim.boredombuster1.activity.repository

import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.model.Result

interface ActivityRemoteDataSource {
    suspend fun getActivity(): Result<Activity>
}