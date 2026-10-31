package com.example.mvvm_c.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CharacterDetailScreen(
    characterId: Int
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Detalle del personaje")
        Text(text = "ID $characterId")
    }
}