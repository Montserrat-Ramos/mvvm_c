package com.example.mvvm_c.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable

@Composable
fun CharacterDetailScreen(
    characterId: Int
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Detalle del personaje")
        Text(text = "ID: $characterId")
    }
}