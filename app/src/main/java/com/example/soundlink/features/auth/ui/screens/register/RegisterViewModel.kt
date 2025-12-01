package com.example.soundlink.features.auth.ui.screens.register

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository
import com.example.soundlink.core.ui.session.SessionViewModel
import com.example.soundlink.features.auth.domain.usecases.RegisterUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val sessionViewModel: SessionViewModel
): ViewModel() {


    fun register(name: String, email: String, pass: String, age: Long, onResult: (Boolean) -> Unit) {

        viewModelScope.launch{
            try{
                if(registerUseCase(name, email, pass, age) != null){
                    sessionViewModel.setUser(User(name, email, pass, age))
                    onResult(true)
                }
                else{
                    onResult(false)

                }
            }catch (e: Exception){

                onResult(false)
                Log.d("RegisterViewModel", "Exception: ", e)
                Log.d("RegisterViewModel", e.printStackTrace().toString() ?: "error desconocido")

            }
           // registerUseCase(name, email, pass, age)
           // sessionViewModel.setUser(User(name, email, pass, age))
        }

    }
}