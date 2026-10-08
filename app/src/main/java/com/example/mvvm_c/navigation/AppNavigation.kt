package com.example.mvvm_c.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvvm_c.presentation.character.CharacterListScreen
import com.example.mvvm_c.presentation.detail.CharacterDetailScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "characters"
    ) {

        composable("characters") {
            CharacterListScreen(
                navController = navController
            )
        }

        composable("detail/{characterId}") { backStackEntry ->
            val characterId =
                backStackEntry.arguments
                    ?.getString("characterId")?.toIntOrNull() ?: 0
            CharacterDetailScreen(characterId = characterId)
        }
    }
}