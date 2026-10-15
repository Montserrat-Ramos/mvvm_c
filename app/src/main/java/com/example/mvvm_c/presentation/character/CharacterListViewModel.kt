package com.example.mvvm_c.presentation.character

import androidx.lifecycle.ViewModel
import com.example.mvvm_c.data.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CharacterListViewModel : ViewModel() {

    private val allCharacters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            image = ""
        ),
        Character(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            image = ""
        ),
        Character(
            id = 3,
            name = "Summer Smith",
            status = "Alive",
            species = "Human",
            image = ""
        )
    )

    private val _uiState = MutableStateFlow(
        CharacterListUIState(character = allCharacters)
    )
    val uiState: StateFlow<CharacterListUIState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        val filteredCharacters = if (query.isBlank()) {
            allCharacters
        } else {
            allCharacters.filter { character ->
                character.name.contains(other = query, ignoreCase = true) ||
                character.species.contains(other = query, ignoreCase = true) ||
                character.status.contains(other = query, ignoreCase = true)
            }
        }

        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                character = filteredCharacters
            )
        }
    }
}
