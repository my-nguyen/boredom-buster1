package eu.maxkim.boredombuster1.activity.fake.usecase

import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.usecase.GetRandomActivity
import eu.maxkim.boredombuster1.model.Result

class FakeGetRandomActivity(private val isSuccessful: Boolean = true) : GetRandomActivity {

    var activity: Activity? = null

    override suspend fun invoke(): Result<Activity> {
        return if (isSuccessful) {
            Result.Success(activity ?: activity1)
        } else {
            Result.Error(RuntimeException("Boom..."))
        }
    }
}

