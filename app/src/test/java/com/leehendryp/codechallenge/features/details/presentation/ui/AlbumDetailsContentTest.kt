package com.leehendryp.codechallenge.features.details.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.leehendryp.codechallenge.features.details.presentation.UIState
import com.leehendryp.codechallenge.features.details.presentation.ui.AlbumDetailsItemContentTestTags.ALBUM_DETAILS_ITEM_CONTENT_IMAGE
import com.leehendryp.codechallenge.features.list.presentation.ui.RetryScreenTestTags
import com.leehendryp.codechallenge.ui.ds.DS_CIRCULAR_PROGRESS_INDICATOR
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlbumDetailsContentTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `when UIState is InitialState should render UI properly`() {
        rule.run {
            setContentWithUIState(UIState.InitialState)

            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsDisplayed()

            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsNotDisplayed()
            onNodeWithTag(ALBUM_DETAILS_ITEM_CONTENT_IMAGE).assertIsNotDisplayed()
        }
    }

    private fun ComposeContentTestRule.setContentWithUIState(uiState: UIState) {
        setContent {
            CodeChallengeTheme {
                AlbumDetailsContent(
                    uiState = uiState,
                ) {}
            }
        }
    }
}
