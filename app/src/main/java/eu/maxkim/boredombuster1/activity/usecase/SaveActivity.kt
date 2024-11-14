package eu.maxkim.boredombuster1.activity.usecase

import eu.maxkim.boredombuster1.activity.model.Activity

interface SaveActivity {
    suspend operator fun invoke(activity: Activity)
}