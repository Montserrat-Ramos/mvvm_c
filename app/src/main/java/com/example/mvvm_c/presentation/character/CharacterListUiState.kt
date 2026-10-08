package com.example.mvvm_c.presentation.character

import com.example.mvvm_c.data.model.DataModel

data class CharacterListUiState (
    val title: String = "Personajes de Rick and Morty",
    val isLoading: Boolean = false,
    val characters: List<DataModel> = emptyList(),
    val error: String? = null
    )