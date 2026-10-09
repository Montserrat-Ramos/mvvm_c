package com.example.mvvm_c.presentation.character

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mvvm_c.data.model.Character

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = viewModel()
) {
        val state by
    viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        //Text(text = state.tittle)

        //Text(text = Lista de personajes)

        //Recorrer el arreglo
        items( state.characters){
            character -> CharacterItem(
                character = character,
                onClick ={
                    navController.navigate(route = "detail/${character.id}")
                }
            )
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit
){
    Card ( modifier = Modifier.fillMaxSize()
        .padding(vertical = 8.dp)
        .clickable{onClick()}
    ){
        Column(modifier = Modifier.padding(16.dp )) {
            Text(text= character.name)
            Text(text= "Especie: ${character.species}")
            Text(text= "Estado: ${character.status}")

        }
    }
}