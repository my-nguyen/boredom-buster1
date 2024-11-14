package eu.maxkim.boredombuster1.activity.ui.favorite

import eu.maxkim.boredombuster1.activity.model.Activity

sealed class FavoritesUiState {
    data class List(val activityList: kotlin.collections.List<Activity>): FavoritesUiState()
    object Empty : FavoritesUiState()
}