package com.example.mvvm_c.presentation.character

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.mvvm_c.data.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharacterListViewModel : ViewModel(){


    private val _uiState = MutableStateFlow(CharacterListUIState()
    )
    val uiState: StateFlow<CharacterListUIState> =
        _uiState.asStateFlow()
    //inicializar

    private fun loadCharacters() {
        val character =listOf(
            Character(
                id=1,
                name= "Rick",
                status="alive",
                species="human",
                image=""
            ),
            Character(
                id=2,
                name= "Morty",
                status="alive",
                species="human",
                image=""
            ),
            Character(
                id=3,
                name= "Summer Smith",
                status="alive",
                species="human",
                image=""
            ),
        )
        _uiState.value =
            CharacterListUIState(
                character=character
            )

    }
}