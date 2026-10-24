package com.example.mvvm_c.presentation.character

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.mvvm_c.model.Character
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CharacterListViewModel: ViewModel() {
    private val allCharacters = listOf(
        Character(
            id = 1,
            name = "Rick Franco",
            status = "Alive",
            species= "Human",
            image = ""
        ),

        Character(
            id = 2,
            name = "Morty Cordova",
            status = "Alive",
            species= "Human",
            image = ""
        ),

        Character(
            id = 3,
            name = "Summer fabricio",
            status = "Alive",
            species= "Human",
            image = ""
        ),
        Character(
            id = 4,
            name = "Summer Raul",
            status = "Alive",
            species= "Human",
            image = ""
        ),
        Character(
            id = 5,
            name = "Summer perez",
            status = "Alive",
            species= "Human",
            image = ""
        ),
    )

    private val _uiState = MutableStateFlow(CharacterListUiState(characters = allCharacters))

    val uiState: StateFlow<CharacterListUiState> =
        _uiState.asStateFlow()

    fun onSearchQueryChange(query: String){
        val filteredCharacter =
            if(query.isBlank()) {
                allCharacters
            }else {
                allCharacters.filter {
                        character -> character.name.contains(query, ignoreCase = true)||
                        character.species.contains(query, ignoreCase = true)||
                        character.status.contains(query, ignoreCase = true)
                }
            }


        _uiState.update {
                state -> state.copy(searchQuery = query,
            characters = filteredCharacter)
        }
    }
}