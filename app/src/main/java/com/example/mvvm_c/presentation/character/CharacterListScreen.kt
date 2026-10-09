package com.example.mvvm_c.presentation.character

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mvvm_c.presentation.component.CharacterCard

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text(
                text = state.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(state.character) { character ->
            CharacterCard(
                character = character,
                onClick = {
                    navController.navigate(route = "detail/${character.id}")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
