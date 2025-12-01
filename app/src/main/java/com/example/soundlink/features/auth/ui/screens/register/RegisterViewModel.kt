package com.example.soundlink.features.auth.ui.screens.register

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


    fun register(name: String, email: String, pass: String, age: Int) {

        viewModelScope.launch{
            try{
                if(registerUseCase(name, email, pass, age)){
                    sessionViewModel.setUser(User(name, email, pass, age))

                }
                else{

                }
            }catch (e: Exception){

            }
           // registerUseCase(name, email, pass, age)
           // sessionViewModel.setUser(User(name, email, pass, age))
        }

    }
}