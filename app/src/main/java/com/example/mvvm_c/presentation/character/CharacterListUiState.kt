package com.example.mvvm_c.presentation.character

import com.example.mvvm_c.model.Character
import java.time.temporal.TemporalQuery

data class CharacterListUiState(
    val title: String = "Personaje de Ricky and Morty",
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val characters: List<Character> = emptyList(),
    val error: String? = null
)
