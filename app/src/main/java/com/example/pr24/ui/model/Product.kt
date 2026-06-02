package com.example.pr24.ui.model

import com.example.pr24.R

data class Product(
    val id: Int,
    val title: String,
    val price: Int,
    val image: Int,
    val description: String,
    val hasSugarOption: Boolean = false,
    val hasCinnamonOption: Boolean = false,
    var count: Int = 1
)

object ProductCatalog {

    val items = listOf(
        Product(
            id = 1,
            title = "Кофе",
            price = 250,
            image = R.drawable.coffee,
            description = "Горячий кофе для бодрого начала дня.",
            hasSugarOption = true,
            hasCinnamonOption = true
        ),
        Product(
            id = 2,
            title = "Чизкейк",
            price = 230,
            image = R.drawable.cheesecake,
            description = "Нежный десерт к кофе или чаю."
        ),
        Product(
            id = 3,
            title = "Чай",
            price = 180,
            image = R.drawable.tea,
            description = "Ароматный чай для спокойного перерыва.",
            hasSugarOption = true
        ),
        Product(
            id = 4,
            title = "Комбо",
            price = 450,
            image = R.drawable.combo,
            description = "Готовый завтрак: напиток и десерт.",
            hasSugarOption = true
        )
    )

    fun findById(id: Int): Product {
        return items.first { it.id == id }
    }
}
