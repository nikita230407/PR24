package com.example.pr24

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pr24.navigation.AppNavGraph
import com.example.pr24.ui.theme.Pr24Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pr24Theme {
                AppNavGraph()
            }
        }
    }
}
