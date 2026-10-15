package com.example.mvvm_c.presentation.character

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(
                modifier = Modifier.height(24.dp)
                )

        Text(
            text = "Personajes",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Explora los personajes de Rick and Morty",
            fontSize = 15.sp,
            // fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = state.searchQuery, onValueChange = {
                text -> viewModel.onSearchQueryChanged(text)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Buscar personaje...")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            }, shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        if (state.characters.isEmpty()) {
            Text(
                text = "No se encontraron personajes",
                modifier = Modifier.padding(top =24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        else{
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = 24.dp
                ), verticalArrangement = Arrangement.spacedBy(12.dp)
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