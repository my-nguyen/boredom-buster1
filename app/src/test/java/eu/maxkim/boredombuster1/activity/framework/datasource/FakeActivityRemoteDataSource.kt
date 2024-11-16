package eu.maxkim.boredombuster1.activity.framework.datasource

import eu.maxkim.boredombuster1.activity.fake.usecase.activity1
import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.repository.ActivityRemoteDataSource
import eu.maxkim.boredombuster1.model.Result

class FakeActivityRemoteDataSource : ActivityRemoteDataSource {

    var getActivityWasCalled = false
        private set

    override suspend fun getActivity(): Result<Activity> {
        getActivityWasCalled = true
        return Result.Success(activity1)
    }
}
