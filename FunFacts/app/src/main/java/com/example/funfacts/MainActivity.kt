package com.example.funfacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.example.funfacts.ui.FunFactScreen
import com.example.funfacts.ui.FunFactViewModel
import com.example.funfacts.ui.FunFactViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: FunFactViewModel by viewModels {
        FunFactViewModelFactory((application as FunFactApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
            ) {
                FunFactScreen(viewModel)
            }
        }
    }
}
