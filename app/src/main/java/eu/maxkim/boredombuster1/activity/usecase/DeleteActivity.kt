package eu.maxkim.boredombuster1.activity.usecase

import eu.maxkim.boredombuster1.activity.model.Activity

interface DeleteActivity {
    suspend operator fun invoke(activity: Activity)
}