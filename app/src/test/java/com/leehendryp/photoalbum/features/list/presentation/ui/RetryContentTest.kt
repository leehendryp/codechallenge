package com.leehendryp.photoalbum.features.list.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.leehendryp.photoalbum.R
import com.leehendryp.photoalbum.ui.theme.PhotoAlbumTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class RetryContentTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `when info is provided and has connection should render item properly`() {
        rule.run {
            val onIntent: () -> Unit = mockk(relaxed = true)

            setContent {
                PhotoAlbumTheme {
                    RetryContent(
                        icon = Icons.Default.DataArray,
                        message = R.string.feed_empty,
                        hasConnection = true,
                    ) {
                        onIntent()
                    }
                }
            }

            onNodeWithText("No data available.").assertIsDisplayed()

            onNodeWithTag(RetryScreenTestTags.ICON).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.CALL_TO_ACTION)
                .assertTextEquals("Retry")
                .performClick()
                .assertIsEnabled()
                .assertIsDisplayed()

            verify(exactly = 1) { onIntent() }
        }
    }

    @Test
    fun `when info is provided and has no connection should render item properly`() {
        rule.run {
            val onIntent: () -> Unit = mockk(relaxed = true)

            setContent {
                PhotoAlbumTheme {
                    RetryContent(
                        icon = Icons.Default.DataArray,
                        message = R.string.feed_empty,
                        hasConnection = false,
                    ) {
                        onIntent()
                    }
                }
            }

            onNodeWithText("No data available.").assertIsDisplayed()

            onNodeWithTag(RetryScreenTestTags.ICON).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsDisplayed()

            onNodeWithTag(RetryScreenTestTags.CALL_TO_ACTION)
                .assertTextEquals("Retry")
                .performClick()
                .assertIsNotEnabled()
                .assertIsDisplayed()

            verify(exactly = 0) { onIntent() }
        }
    }
}
