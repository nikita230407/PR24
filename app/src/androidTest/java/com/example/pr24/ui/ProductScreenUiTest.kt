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
class ProductScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun productScreenShowsMainElements() {
        composeRule.setContent {
            AppNavGraph()
        }

        composeRule
            .onNodeWithTag("product_card_1")
            .performClick()

        composeRule
            .onNodeWithTag("product_title")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("product_description")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("product_add_button")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag("product_back_button")
            .assertIsDisplayed()
    }
}
