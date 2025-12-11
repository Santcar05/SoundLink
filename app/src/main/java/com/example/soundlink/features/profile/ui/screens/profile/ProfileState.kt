package com.example.soundlink.features.profile.ui.screens.profile

import com.example.soundlink.core.domain.model.User

data class ProfileFormErrors(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val age: String? = null,
)

data class ProfileUiState(
    val user: User = User(),
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val formErrors: ProfileFormErrors = ProfileFormErrors()
) {
    val isIdle: Boolean get() = !isSaving && successMessage == null && errorMessage == null
}