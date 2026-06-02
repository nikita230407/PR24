package com.example.pr24.ui

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
class BasketScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun basketScreenShowsMainElements() {
        composeRule.setContent {
            AppNavGraph()
        }

        composeRule
            .onNodeWithTag("catalog_button")
            .performClick()

        composeRule
            .onNodeWithTag("basket_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("basket_total")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("basket_back_button")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("buy_button")
            .assertIsDisplayed()
    }
}
