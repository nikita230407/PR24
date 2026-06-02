package com.example.pr24.ux

import androidx.compose.ui.test.assertIsOn
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
class InteractionTest {

    @get:Rule
    val composeRule =
        createComposeRule()

    @Test
    fun inputFieldsWorkCorrectly() {
        composeRule.setContent {
            AppNavGraph()
        }

        composeRule
            .onNodeWithTag("product_card_1")
            .performClick()

        composeRule
            .onNodeWithTag("sugar_switch")
            .performClick()

        composeRule
            .onNodeWithTag("sugar_switch")
            .assertIsOn()

        composeRule
            .onNodeWithTag("cinnamon_switch")
            .performClick()

        composeRule
            .onNodeWithTag("cinnamon_switch")
            .assertIsOn()

        composeRule
            .onNodeWithTag("product_add_button")
            .performClick()

        composeRule
            .onNodeWithTag("basket_title")
            .assertIsDisplayed()
    }
}
