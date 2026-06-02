package com.example.pr24.ui.model

import androidx.compose.runtime.mutableStateListOf

object CartManager {

    val products = mutableStateListOf<Product>()

    fun addProduct(product: Product) {

        val index =
            products.indexOfFirst { it.id == product.id }

        if (index != -1) {
            val existing = products[index]
            products[index] = existing.copy(
                count = existing.count + 1
            )
        }
        else {
            products.add(
                product.copy(count = 1)
            )
        }
    }

    fun increase(product: Product) {
        val index =
            products.indexOfFirst { it.id == product.id }

        if (index != -1) {
            products[index] = products[index].copy(
                count = products[index].count + 1
            )
        }
    }

    fun decrease(product: Product) {
        val index =
            products.indexOfFirst { it.id == product.id }

        if (index != -1 && products[index].count > 1) {
            products[index] = products[index].copy(
                count = products[index].count - 1
            )
        }
    }

    fun itemsCount(): Int {
        return products.sumOf { it.count }
    }

    fun totalPrice(): Int {

        return products.sumOf {
            it.price * it.count
        }
    }

    fun clear() {
        products.clear()
    }
}
