package com.leehendryp.codechallenge.features.details.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.leehendryp.codechallenge.features.common.domain.MockDomainModels
import com.leehendryp.codechallenge.features.details.presentation.AlbumDetailsPresenter
import com.leehendryp.codechallenge.features.details.presentation.UIState
import com.leehendryp.codechallenge.features.details.presentation.ui.AlbumDetailsItemContentTestTags.ALBUM_DETAILS_ITEM_CONTENT_COLUMN
import com.leehendryp.codechallenge.features.details.presentation.ui.AlbumDetailsItemContentTestTags.ALBUM_DETAILS_ITEM_CONTENT_IMAGE
import com.leehendryp.codechallenge.features.list.presentation.ui.RetryScreenTestTags
import com.leehendryp.codechallenge.ui.ds.DS_CIRCULAR_PROGRESS_INDICATOR
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlbumDetailsScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val presenter: AlbumDetailsPresenter = mockk(relaxed = true)

    @Test
    fun `when UIState is InitialState should render UI properly`() {
        rule.run {
            setContentWithUIState(UIState.InitialState)

            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsDisplayed()

            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsNotDisplayed()
            onNodeWithTag(ALBUM_DETAILS_ITEM_CONTENT_COLUMN).assertIsNotDisplayed()
        }
    }

    @Test
    fun `when UIState is Retry should render UI properly`() {
        rule.run {
            setContentWithUIState(UIState.Retry)

            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsDisplayed()

            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(ALBUM_DETAILS_ITEM_CONTENT_COLUMN).assertIsNotDisplayed()
        }
    }

    @Test
    fun `when UIState is Content should render UI properly`() {
        rule.run {
            setContentWithUIState(UIState.Content(MockDomainModels.mockAlbum1))

            onNodeWithTag(ALBUM_DETAILS_ITEM_CONTENT_COLUMN).assertIsDisplayed()
            onNodeWithTag(ALBUM_DETAILS_ITEM_CONTENT_IMAGE).assertIsDisplayed()
            onNodeWithText(MockDomainModels.mockAlbum1.title).assertIsDisplayed()

            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsNotDisplayed()
        }
    }

    private fun ComposeContentTestRule.setContentWithUIState(uiState: UIState) {
        every { presenter.uiState } returns MutableStateFlow(uiState)

        setContent {
            CodeChallengeTheme {
                AlbumDetailsScreen(
                    albumDetailsId = 1,
                    presenter = presenter,
                )
            }
        }
    }
}
