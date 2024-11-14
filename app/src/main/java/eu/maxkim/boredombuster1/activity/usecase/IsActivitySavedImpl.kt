package eu.maxkim.boredombuster1.activity.usecase

import javax.inject.Inject

class IsActivitySavedImpl @Inject constructor(
    private val activityRepository: ActivityRepository
) : IsActivitySaved {

    override suspend fun invoke(key: String): Boolean {
        return activityRepository.isActivitySaved(key)
    }
}