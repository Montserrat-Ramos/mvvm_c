package com.example.mvvm_c.presentation.character

import com.example.mvvm_c.data.model.Character

data class CharacterListUiState(
    val title: String = "Personajes de Ricky and Morty",
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: String? = null
)