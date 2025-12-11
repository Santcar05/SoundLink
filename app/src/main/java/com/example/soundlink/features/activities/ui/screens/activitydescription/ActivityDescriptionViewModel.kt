package com.example.soundlink.features.activities.ui.screens.activitydescription

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ActivityDescriptionViewModel(
    activity: ActivityDetail
) : ViewModel() {
    private val _state = MutableStateFlow(
        ActivityDescriptionState(activity = activity)
    )
    val state: StateFlow<ActivityDescriptionState> = _state

    fun selectSection(section: ActivityDetailSection) {
        _state.update { it.copy(selectedSection = section) }
    }
}