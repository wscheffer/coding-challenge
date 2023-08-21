@file:OptIn(ExperimentalMaterial3Api::class)

package za.co.retrorabbit.gameofthrones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import za.co.retrorabbit.gameofthrones.router.Router
import za.co.retrorabbit.gameofthrones.ui.theme.GameOfThronesTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            GameOfThronesTheme {
                MainScreen()
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        Router()
    }
}




