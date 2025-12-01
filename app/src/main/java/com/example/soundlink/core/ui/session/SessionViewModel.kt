package com.example.soundlink.core.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository
import com.example.soundlink.core.domain.usecases.GetUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SessionViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SessionState())
    var state = _state.asStateFlow()

    fun setUser(user: User) {
        _state.value = _state.value.copy(
            user = user,
            isLoggedIn = true
        )
    }

    fun logout() {
        _state.value = SessionState()
    }



}
