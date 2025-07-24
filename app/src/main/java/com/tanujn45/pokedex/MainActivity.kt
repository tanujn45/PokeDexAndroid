package com.tanujn45.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tanujn45.pokedex.ui.navigation.MainNavGraph
import com.tanujn45.pokedex.ui.theme.PokeDexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            PokeDexTheme {
                MainNavGraph()
            }
        }
    }
}
