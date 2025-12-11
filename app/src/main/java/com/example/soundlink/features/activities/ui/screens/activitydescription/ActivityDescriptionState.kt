package com.example.soundlink.features.activities.ui.screens.activitydescription

data class ActivityDetail(
    val imageRes: Int,
    val name: String,
    val description: String,
    val rules: List<String>,
    val prizes: List<String>
)

data class ActivityDescriptionState(
    val activity: ActivityDetail,
    val selectedSection: ActivityDetailSection = ActivityDetailSection.General
)