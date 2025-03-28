package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.leehendryp.codechallenge.features.list.presentation.UIState
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.ALBUM_LIST_LAZY_COLUMN
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.EMPTY_STATE
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.PROGRESS_INDICATOR
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.TOP_APP_BAR
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlbumListContentTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `when UIState is InitialState should render UI properly`() {
        rule.run {
            setContentWithUIState(UIState.InitialState)

            onNodeWithTag(TOP_APP_BAR).assertIsDisplayed()
            onNodeWithTag(PROGRESS_INDICATOR).assertIsDisplayed()
            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsNotDisplayed()
            onNodeWithTag(EMPTY_STATE).assertIsNotDisplayed()
        }
    }

    @Test
    fun `when UIState Status is Content should render UI properly`() {
        rule.run {
            setContentWithUIState(
                uiState = UIState(
                    status = UIState.Status.Content(
                        models = emptyList(),
                    ),
                ),
            )

            onNodeWithTag(TOP_APP_BAR).assertIsDisplayed()
            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsDisplayed()
            onNodeWithTag(PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(EMPTY_STATE).assertIsNotDisplayed()
        }
    }

    @Test
    fun `when UIState Status is Empty should render UI properly`() {
        rule.run {
            setContentWithUIState(
                uiState = UIState(
                    status = UIState.Status.Empty,
                ),
            )

            onNodeWithTag(TOP_APP_BAR).assertIsDisplayed()
            onNodeWithTag(EMPTY_STATE).assertIsDisplayed()
            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsNotDisplayed()
            onNodeWithTag(PROGRESS_INDICATOR).assertIsNotDisplayed()
        }
    }

    private fun ComposeContentTestRule.setContentWithUIState(uiState: UIState) {
        setContent {
            CodeChallengeTheme {
                AlbumListContent(uiState = uiState) {}
            }
        }
    }
}
