package com.leehendryp.codechallenge.ui.ds

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class DSCircularProgressIndicatorTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `should render item properly`() {
        rule.run {
            setContent {
                CodeChallengeTheme {
                    DSCircularProgressIndicator()
                }
            }
            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsDisplayed()
        }
    }
}
