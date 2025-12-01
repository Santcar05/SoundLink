package com.example.soundlink.features.auth.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository
import com.example.soundlink.core.domain.usecases.GetUserUseCase
import com.example.soundlink.core.ui.session.SessionViewModel
import com.example.soundlink.features.auth.domain.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getCurrentUser: GetUserUseCase

): ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()



    fun login( email: String, password: String, sessionViewModel: SessionViewModel, onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = LoginState(isLoading = true)

            try {
                val result = loginUseCase(email, password)
                if (result) {
                    //Find and Assign user to session
                    val user = getCurrentUser(email)
                    if (user != null) {
                        sessionViewModel.setUser(user)
                    }

                    onLoginSuccess()

                    _uiState.value = LoginState(isLoading = false)
                    onLoginSuccess()
                } else {
                    _uiState.value = LoginState(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = LoginState(isLoading = false)
            }
        }
    }
}