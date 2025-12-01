package com.example.soundlink.features.auth.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundlink.core.data.datasource.retrofitspringboot.RetrofitClient
import com.example.soundlink.core.data.dto.LoginRequest
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



    fun login(
        email: String,
        password: String,
        sessionViewModel: SessionViewModel,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = LoginState(isLoading = true)

            try {
                //val result = loginUseCase(email, password)
                val user = loginUseCase(email, password)
                val result = user != null

                if (result) {



                    sessionViewModel.setUser(user!!)


                    onResult(true)
                } else {
                    onResult(false)
                }

            } catch (e: Exception) {
            e.printStackTrace()
            Log.e("LOGIN_ERROR", e.message ?: "error desconocido")
                Log.e("LOGIN_ERROR", "Exception: ", e)
            onResult(false)
        }
        finally {
                _uiState.value = LoginState(isLoading = false)
            }
        }
    }

}