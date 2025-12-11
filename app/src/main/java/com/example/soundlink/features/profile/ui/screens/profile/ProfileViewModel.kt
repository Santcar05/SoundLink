package com.example.soundlink.features.profile.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.ui.session.SessionViewModel
import com.example.soundlink.features.profile.usecases.UpdateUserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Perfil (ver / editar).
 * - En un caso real inyecta use-cases (GetUser, UpdateUser).
 * - Aquí hay lógica de validación y simulación de guardado.
 */
class ProfileViewModel(
    // En producción sustituir por use-cases inyectables
    private val sessionViewModel: SessionViewModel,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(user = sessionViewModel.state.value.user ?: sampleUser(), isEditing = false)
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Cambia el modo edición/visualización
    fun toggleEdit() {
        _uiState.update { current ->
            // limpiar mensajes al entrar/salir
            current.copy(
                isEditing = !current.isEditing,
                successMessage = null,
                errorMessage = null,
                formErrors = ProfileFormErrors()
            )
        }
    }

    // Actualiza campos del formulario (se envía el User completo parcial)
    fun updateName(newName: String) = updateUserCopy { it.copy(name = newName) }
    fun updateEmail(newEmail: String) = updateUserCopy { it.copy(email = newEmail) }
    fun updatePassword(newPassword: String) = updateUserCopy { it.copy(password = newPassword) }
    fun updateAge(newAge: Int) = updateUserCopy { it.copy(age = newAge) }
    fun updateAvatarUrl(newUrl: String) = updateUserCopy { it.copy(avatarUrl = newUrl) }
    fun updateVerified(verified: Boolean) = updateUserCopy { it.copy(verified = verified) }

    private fun updateUserCopy(transform: (User) -> User) {
        _uiState.update { it.copy(user = transform(it.user), successMessage = null, errorMessage = null) }
    }

    // Validación básica (puedes extenderla)
    private fun validate(user: User): ProfileFormErrors {
        val errors = ProfileFormErrors()
        var nameErr: String? = null
        var emailErr: String? = null
        var passwordErr: String? = null
        var ageErr: String? = null

        if (user.name.isBlank()) nameErr = "El nombre no puede estar vacío"
        if (user.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches())
            emailErr = "Email inválido"
        if (user.password.length < 6) passwordErr = "La contraseña debe tener al menos 6 caracteres"
        if (user.age <= 0 || user.age > 120) ageErr = "Edad inválida"

        return ProfileFormErrors(
            name = nameErr,
            email = emailErr,
            password = passwordErr,
            age = ageErr
        )
    }

    // Guardar cambios (simulado). Reemplazar con tu lógica real.
    fun saveProfile(
    ) {
        val current = _uiState.value
        val user = current.user
        val formErrors = validate(user)

        // Si hay errores, los ponemos en el estado y no procedemos
        if (listOf(formErrors.name, formErrors.email, formErrors.password, formErrors.age).any { it != null }) {
            _uiState.update { it.copy(formErrors = formErrors, successMessage = null, errorMessage = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, successMessage = null, errorMessage = null) }

            try {
                // Simula petición de red / persistencia
                delay(1100)
                // En caso real, llamar a use-case: updateUser(user)
                updateUserUseCase(user)
                sessionViewModel.updateUser(user)

                //Actualizar  usuario


                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isEditing = false,
                        successMessage = "Perfil guardado correctamente",
                        errorMessage = null,
                        formErrors = ProfileFormErrors()
                    )
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        successMessage = null,
                        errorMessage = "Error al guardar. Intenta nuevamente."
                    )
                }
            }
        }
    }

    // Revertir cambios (simplemente recargar desde fuente original)
    fun discardChanges(original: User) {
        _uiState.update { it.copy(user = original, isEditing = false, formErrors = ProfileFormErrors(), successMessage = null, errorMessage = null) }
    }

    companion object {
        private fun sampleUser() = User(
            id = 42,
            name = "María Gómez",
            email = "maria@example.com",
            password = "secret123",
            age = 29,
            avatarUrl = "", // placeholder
            verified = true
        )
    }
}