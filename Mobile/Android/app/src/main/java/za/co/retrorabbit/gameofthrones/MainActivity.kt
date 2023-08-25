package za.co.retrorabbit.gameofthrones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import za.co.retrorabbit.gameofthrones.router.Router
import za.co.retrorabbit.gameofthrones.ui.theme.GameOfThronesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameOfThronesTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        Router()
    }
}
