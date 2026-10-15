package com.example.mvvm_c.presentation.character

import androidx.lifecycle.ViewModel
import com.example.mvvm_c.data.model.DataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharacterListViewModel : ViewModel() {

    private val allCharacters = listOf(
        DataModel(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Humano",
            image = ""
        ),
        DataModel(
            id = 2,
            name = "Morty Sanchez",
            status = "Alive",
            species = "Humano",
            image = ""
        ),
        DataModel(
            id = 3,
            name = "Summer Sanchez",
            status = "Alive",
            species = "Humano",
            image = ""
        )
    )

    private val _uiState = MutableStateFlow(CharacterListUiState(characters = allCharacters))
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        val filteredCharacters = if (query.isBlank()) {
            allCharacters
        } else {
            allCharacters.filter { character ->
                character.name.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = CharacterListUiState(characters = filteredCharacters)
    }
}
