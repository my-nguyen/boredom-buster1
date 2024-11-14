package eu.maxkim.boredombuster1.activity.usecase

import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.model.Result

interface GetRandomActivity {
    suspend operator fun invoke(): Result<Activity>
}