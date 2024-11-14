package eu.maxkim.boredombuster1.activity.usecase

import androidx.lifecycle.LiveData
import eu.maxkim.boredombuster1.activity.model.Activity

interface GetFavoriteActivities {
    operator fun invoke(): LiveData<List<Activity>>
}