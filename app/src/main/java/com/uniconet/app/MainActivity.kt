package com.uniconet.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.uniconet.app.navigation.AppNavigation
import com.uniconet.app.ui.theme.UniconetTheme

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UniconetTheme {
                AppNavigation()
            }
        }
    }
}