package com.example.pr24.ui.model

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartManagerTest {

    private val coffee = Product(
        id = 1,
        title = "Кофе",
        price = 250,
        image = 0,
        description = "Тестовый кофе"
    )

    @Before
    fun setUp() {
        CartManager.clear()
    }

    @After
    fun tearDown() {
        CartManager.clear()
    }

    @Test
    fun addProductIncreasesCountAndTotalPrice() {
        CartManager.addProduct(coffee)
        CartManager.addProduct(coffee)

        assertEquals(2, CartManager.itemsCount())
        assertEquals(500, CartManager.totalPrice())
    }

    @Test
    fun decreaseDoesNotGoBelowOne() {
        CartManager.addProduct(coffee)
        CartManager.decrease(coffee)

        assertEquals(1, CartManager.itemsCount())
        assertEquals(250, CartManager.totalPrice())
    }
}
