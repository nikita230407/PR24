package com.example.pr24.navigation

object Routes {

    const val CATALOG = "catalog"
    const val PRODUCT = "product"
    const val MAP = "map"
    const val BASKET = "basket"

    fun productRoute(productId: Int): String {
        return "$PRODUCT/$productId"
    }
}
