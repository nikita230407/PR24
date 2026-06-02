package com.example.pr24.ux

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pr24.navigation.AppNavGraph
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun navigationBetweenScreens() {
        composeRule.setContent {
            AppNavGraph()
        }

        composeRule
            .onNodeWithTag("catalog_map_button")
            .performClick()

        composeRule
            .onNodeWithTag("map_back_button")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("map_back_button")
            .performClick()

        composeRule
            .onNodeWithTag("catalog_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("product_card_1")
            .performClick()

        composeRule
            .onNodeWithTag("product_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("product_back_button")
            .performClick()

        composeRule
            .onNodeWithTag("catalog_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("product_card_1")
            .performClick()

        composeRule
            .onNodeWithTag("product_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("product_add_button")
            .performClick()

        composeRule
            .onNodeWithTag("basket_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("basket_back_button")
            .assertIsDisplayed()
    }
}
