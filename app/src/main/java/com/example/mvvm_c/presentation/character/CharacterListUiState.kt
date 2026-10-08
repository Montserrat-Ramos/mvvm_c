package com.example.mvvm_c.presentation.character

import com.example.mvvm_c.data.model.Character

data class CharacterListUiState (
    val title: String = "Personas de Rick and Morty",
    val isLoding: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: String? = null
)
