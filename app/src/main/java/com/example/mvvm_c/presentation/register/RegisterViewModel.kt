package com.example.mvvm_c.presentation.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onRegisterSubmitted() {
        if (_uiState.value.name.isBlank() ||
            _uiState.value.email.isBlank() ||
            _uiState.value.password.isBlank()
        ) {
            _uiState.update { it.copy(errorMessage = "Por favor completa todos los campos") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        _uiState.update {
            it.copy(
                isLoading = false,
                isSuccess = true
            )
        }
    }
}