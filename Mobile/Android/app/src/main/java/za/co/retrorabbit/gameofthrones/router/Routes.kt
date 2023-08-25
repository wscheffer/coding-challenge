package za.co.retrorabbit.gameofthrones.router

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import za.co.retrorabbit.gameofthrones.screens.BookScaffold
import za.co.retrorabbit.gameofthrones.screens.CharacterScaffold
import za.co.retrorabbit.gameofthrones.screens.HouseDetailScaffold
import za.co.retrorabbit.gameofthrones.screens.HousesListScaffold
import za.co.retrorabbit.gameofthrones.screens.housesData

@Composable
fun Router() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = RouteHome) {
        composable(RouteHome) {
            HousesListScaffold(navController)
        }
        composable(
            routeHouseDetails(),
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            }),
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val id = arguments.getInt("id")
            HouseDetailScaffold(
                housesData.data.value?.find { it.url?.endsWith("/${id}") == true },
                navController
            )
        }
        composable(
            routeCharacter(),
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            }),
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val id = arguments.getInt("id")
            CharacterScaffold(
                id, navController
            )
        }
        composable(
            routeBook(),
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            }),
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val id = arguments.getInt("id")
            BookScaffold(
                id, navController
            )
        }
    }
}

const val RouteHome = "home"

fun route(navController: NavController, url: String?) {
    val parts = url?.split("/")

    when (parts?.get(4)) {
        "houses" -> routeHouseDetails(parts.last().toInt())
        "characters" -> routeCharacter(parts.last().toInt())
        "books" -> routeBook(parts.last().toInt())
        else -> null
    }?.let {
        navController.navigate(it)
    }
}

private fun routeHouseDetails(id: Int? = null): String {
    return "house-details/id=${id ?: "{id}"}"
}

private fun routeCharacter(id: Int? = null): String {
    return "character/id=${id ?: "{id}"}"
}

private fun routeBook(id: Int? = null): String {
    return "book/id=${id ?: "{id}"}"
}
