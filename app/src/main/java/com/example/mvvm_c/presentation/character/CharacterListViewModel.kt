package com.example.mvvm_c.presentation.character

import androidx.lifecycle.ViewModel
import com.example.mvvm_c.data.model.DataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.foundation.lazy.items

class CharacterListViewModel: ViewModel() {
    private val _uiState =
        MutableStateFlow( value = CharacterListUIState() )

    val uiState: StateFlow<CharacterListUIState> =
        _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        val characters = listOf(
            DataModel(
                1, "Rick Sanchez", "Alive", "Human", ""
            ),
            DataModel(
                2, "Morty Smith", "Alive", "Human", ""
            ),
            DataModel(
                3, "Summer Smith", "Alive", "Human", ""
            ),
        )

        _uiState.value = CharacterListUIState(characters = characters)
    }
}