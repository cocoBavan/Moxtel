package com.bltech.moxtel.features

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.bltech.moxtel.global.MainActivity
import org.junit.Rule
import org.junit.Test

class MoxtelIntegrationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `when a movie is clicked assert its details screen is opened`() {
        composeTestRule.waitUntil(5000) {
            composeTestRule.onNodeWithTag("gallery").isDisplayed()
        }
        composeTestRule.onNodeWithTag("gallery").onChildAt(0).performClick()
        composeTestRule.waitUntil(5000) {
            composeTestRule.onNodeWithTag("detail_view").isDisplayed()
        }
        composeTestRule.onNodeWithTag("detail_view").assertIsDisplayed()
    }
}
