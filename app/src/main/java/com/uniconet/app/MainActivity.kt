package com.uniconet.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.uniconet.app.navigation.AppNavigation
import com.uniconet.app.ui.theme.UniconetTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UniconetTheme {
                AppNavigation()
            }
        }
    }
}