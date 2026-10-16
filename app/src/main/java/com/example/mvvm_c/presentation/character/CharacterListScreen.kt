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
import com.example.mvvm_c.components.CharacterCard
import com.example.mvvm_c.data_model.Character

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier =  Modifier.fillMaxWidth()
        .padding(horizontal = 16.dp))
    {
        Spacer( modifier = Modifier.height(24.dp))
        Text(text = "Personajes",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground)

        Spacer( modifier = Modifier.height(20.dp))
        Text(text = "Explorar los personajes de Rick and Morty",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer( modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { text ->
                viewModel.onSearchQueryChange(text)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Buscar Personaje...")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            shape = RoundedCornerShape(size = 16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.characters.isEmpty()){
            Text(text = "No se encontraron personajes",
                modifier = Modifier.padding(top = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.characters){ character ->
                    CharacterCard(
                        character = character,
                        onClick = {
                            navController
                                .navigate(route = "detail/${character.id}")
                        }
                    )
                }
            }
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