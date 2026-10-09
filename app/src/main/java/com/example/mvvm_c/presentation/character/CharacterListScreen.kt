package com.example.mvvm_c.presentation.character

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.mvvm_c.data.model.DataModel
import com.example.mvvm_c.presentation.components.CharacterCard

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(items = state.characters) {
            character ->
            CharacterCard (
                character = character,
                onClick = {
                    navController.navigate(route = "detail/${character.id}")
                }
            )
        }
    }
}

@Composable
fun CharacterItem(
    character: DataModel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().
        padding(vertical = 8.dp).
        clickable { onClick() }
    ) {
        Column() {
            Text(
                text = "Nombre: ${character.name}",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Estatus: ${character.status}",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Especie: ${character.species}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}