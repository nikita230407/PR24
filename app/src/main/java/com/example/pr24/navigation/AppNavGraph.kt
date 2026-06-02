package com.example.pr24.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pr24.ui.model.ProductCatalog
import com.example.pr24.ui.screens.BasketScreen
import com.example.pr24.ui.screens.CatalogScreen
import com.example.pr24.ui.screens.MapScreen
import com.example.pr24.ui.screens.ProductScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.CATALOG
    ) {

        composable(Routes.CATALOG) {

            CatalogScreen(
                onBasketClick = {
                    navController.navigate(Routes.BASKET)
                },
                onMapClick = {
                    navController.navigate(Routes.MAP)
                },
                onProductClick = { productId ->
                    navController.navigate(Routes.productRoute(productId))
                }
            )
        }

        composable(
            route = "${Routes.PRODUCT}/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments?.getInt("productId") ?: 1

            ProductScreen(
                product = ProductCatalog.findById(productId),
                onAddToCartClick = {
                    navController.navigate(Routes.BASKET)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.MAP) {

            MapScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.BASKET) {

            BasketScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
