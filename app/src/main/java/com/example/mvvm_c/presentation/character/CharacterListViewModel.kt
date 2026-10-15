package com.example.mvvm_c.presentation.character

import androidx.lifecycle.ViewModel
import com.example.mvvm_c.data.model.DataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CharacterListViewModel: ViewModel() {

    private val allCharacters = listOf(
        DataModel(
            1, "Rick Sanchez", "Alive", "Human", ""
        ),
        DataModel(
            2, "Morty Smith", "Alive", "Human", ""
        ),
        DataModel(
            3, "Summer Smith", "Alive", "Human", ""
        ),
        DataModel(
            4, "Beth Smith", "Alive", "Human", ""
        ),
        DataModel(
            5, "Jerry Smith", "Alive", "Human", ""
        )
    )

    private val _uiState =
        MutableStateFlow(
            CharacterListUIState(characters = allCharacters)
        )

    val uiState: StateFlow<CharacterListUIState> =
        _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        val filteredCharacter =
            if (query.isBlank()) {
                allCharacters
            }
            else {
                allCharacters.filter { character ->
                    character.name.contains(query, ignoreCase = true)
                }
            }

        _uiState.update {
            state -> state.copy(
                searchQuery = query,
                characters = filteredCharacter
            )
        }

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            characters = filteredCharacter
        )
    }



//    init {
//        loadCharacters()
//    }

//    private fun loadCharacters() {
//        val characters = listOf(
//            DataModel(
//                1, "Rick Sanchez", "Alive", "Human", ""
//            ),
//            DataModel(
//                2, "Morty Smith", "Alive", "Human", ""
//            ),
//            DataModel(
//                3, "Summer Smith", "Alive", "Human", ""
//            ),
//            DataModel(
//                4, "Beth Smith", "Alive", "Human", ""
//            ),
//            DataModel(
//                5, "Jerry Smith", "Alive", "Human", ""
//            )
//        )

//        _uiState.value = CharacterListUIState(characters = characters)
//    }
}