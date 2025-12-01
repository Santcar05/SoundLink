package com.example.soundlink.core.ui.session

import com.example.soundlink.core.domain.model.User

data class SessionState(
    val isLoggedIn: Boolean = false,
    val user: User? = null
)
