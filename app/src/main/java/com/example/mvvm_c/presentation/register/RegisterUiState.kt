package com.example.mvvm_c.presentation.register

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password : String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)