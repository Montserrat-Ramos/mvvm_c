package com.example.mvvm_c.presentation.character

import androidx.lifecycle.ViewModel
import com.example.mvvm_c.data.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharacterListViewModel: ViewModel() {
    private val _uiState =
        MutableStateFlow( value = CharacterListUiState()
        )
    val uiState: StateFlow<CharacterListUiState> =
            _uiState.asStateFlow()

    //Inicializar
     init {
         loadCharacters()
     }

    private fun loadCharacters(){
        val characters = listOf(
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
        _uiState.value =
            CharacterListUiState(
                characters = characters)
    }
}