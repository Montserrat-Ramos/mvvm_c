package com.example.mvvm_c.presentation.character

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.mvvm_c.componetns.CharacterCard
import com.example.mvvm_c.data_model.Character

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn (
        modifier = Modifier.padding(16.dp)
    ) {
        items(state.character) { character ->
            CharacterCard(
                character = character,
                onClick = {
                    navController
                        .navigate("detail/${character.id}")
                }
            )
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()
        .padding(vertical = 8.dp)
        .clickable{(onClick())}
    ) {
        Column( modifier = Modifier.padding(all = 16.dp)) {
            Text(text = character.name)
            Text(text = "Especie: ${character.species}")
            Text(text = "Status: ${character.status}")

        }
    }
}