package eu.maxkim.boredombuster1.activity.ui.newactivity

import eu.maxkim.boredombuster1.activity.model.Activity

sealed class NewActivityUiState {
    data class Success(val activity: Activity, val isFavorite: Boolean): NewActivityUiState()
    data class Error(val exception: Throwable): NewActivityUiState()
    object Loading : NewActivityUiState()
}