package com.example.mvvm_c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mvvm_c.navigation.AppNavigation
import com.example.mvvm_c.ui.theme.MyApplicationInitialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationInitialTheme {
                AppNavigation()
            }
        }
    }
}