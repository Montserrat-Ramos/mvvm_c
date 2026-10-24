package com.example.mvvm_c.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvvm_c.presentation.character.CharacterListScreen
import com.example.mvvm_c.presentation.detail.CharacterDetailScreen
import com.example.mvvm_c.presentation.login.Login // <--- Importamos tu Login

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login" // <--- Definimos que la app inicie en el Login
    ) {
        // 1. Ruta del Login
        composable("login") {
            Login(
                onLoginSuccess = {
                    // Al iniciar sesión con éxito, navegamos a "characters"
                    // y borramos el login del historial para que no regrese con el botón "Atras"
                    navController.navigate("characters") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 2. Ruta de la lista de personajes
        composable("characters") {
            CharacterListScreen(
                navController = navController
            )
        }

        // 3. Ruta del detalle del personaje
        composable("detail/{characterId}") { backStackEntry ->
            val characterId =
                backStackEntry.arguments
                    ?.getString("characterId")
                    ?.toIntOrNull()
                    ?: 0
            CharacterDetailScreen(characterId = characterId)
        }
    }
}