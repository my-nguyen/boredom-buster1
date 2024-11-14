package eu.maxkim.boredombuster1.activity.usecase

import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.model.Result
import javax.inject.Inject

class GetRandomActivityImpl @Inject constructor(
    private val activityRepository: ActivityRepository
) : GetRandomActivity {

    override suspend fun invoke(): Result<Activity> {
        return activityRepository.getNewActivity()
    }
}