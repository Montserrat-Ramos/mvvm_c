package com.example.mvvm_c.presentation.character

import com.example.mvvm_c.data_model.Character

data class CharacterListUiState(
    val title: String = "Personajes de Ricky and Morty",
    val isLoading: Boolean = false,
    val character: List<Character> = emptyList()
)