package com.example.pr24.ux

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pr24.navigation.AppNavGraph
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SaveDataTest {

    @get:Rule
    val composeRule =
        createComposeRule()

    @Test
    fun saveButtonSavesData() {
        composeRule.setContent {
            AppNavGraph()
        }

        composeRule
            .onNodeWithTag("catalog_button")
            .performClick()

        composeRule
            .onNodeWithTag("age_input")
            .performTextInput("18")

        composeRule
            .onNodeWithTag("age_input")
            .assertTextContains("18")

        composeRule
            .onNodeWithTag("name_input")
            .performTextInput("Ivan")

        composeRule
            .onNodeWithTag("name_input")
            .assertTextContains("Ivan")

        composeRule
            .onNodeWithTag("buy_button")
            .performScrollTo()
            .performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule
                .onAllNodesWithTag("order_status")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule
            .onNodeWithTag("order_status")
            .performScrollTo()
            .assertTextContains("Ivan, 18,", substring = true)
    }
}
