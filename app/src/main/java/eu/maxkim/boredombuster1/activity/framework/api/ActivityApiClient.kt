package eu.maxkim.boredombuster1.activity.framework.api

import eu.maxkim.boredombuster1.activity.model.Activity
import retrofit2.http.GET

interface ActivityApiClient {
    @GET("/api/activity")
    suspend fun getActivity(): Activity
}