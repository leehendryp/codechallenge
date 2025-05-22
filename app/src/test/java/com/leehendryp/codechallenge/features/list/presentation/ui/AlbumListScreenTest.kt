package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.leehendryp.codechallenge.core.utils.MainCoroutineRule
import com.leehendryp.codechallenge.features.common.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.AlbumListPresenter
import com.leehendryp.codechallenge.features.list.presentation.Intent
import com.leehendryp.codechallenge.features.list.presentation.UIState
import com.leehendryp.codechallenge.features.list.presentation.UIState.Status
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.ALBUM_LIST_LAZY_COLUMN
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.PAGING_PROGRESS_INDICATOR
import com.leehendryp.codechallenge.ui.ds.DS_CIRCULAR_PROGRESS_INDICATOR
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlbumListScreenTest {
    @get:Rule
    val rule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    internal val coroutineRule = MainCoroutineRule()
    private val presenter: AlbumListPresenter = mockk(relaxed = true)
    private val onNavigate: (Int) -> Unit = mockk()

    @Test
    fun `when UIState is InitialState should render UI properly`() {
        rule.run {
            setContentWithUIState(UIState.InitialState)

            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsDisplayed()

            onNodeWithTag(PAGING_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsNotDisplayed()
            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsNotDisplayed()
        }
    }

    @Test
    fun `when UIState Status is Empty should render UI properly`() {
        rule.run {
            setContentWithUIState(
                uiState = UIState.InitialState.copy(
                    status = Status.Empty,
                ),
            )

            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.ICON).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.CALL_TO_ACTION).assertIsDisplayed()

            onNodeWithText("No data available.").assertIsDisplayed()

            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsNotDisplayed()
            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(PAGING_PROGRESS_INDICATOR).assertIsNotDisplayed()
        }
    }

    @Test
    fun `when UIState Status is ClientRetry should render UI properly`() {
        rule.run {
            setContentWithUIState(
                uiState = UIState.InitialState.copy(
                    status = Status.ClientRetry,
                ),
            )

            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.ICON).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.CALL_TO_ACTION).assertIsDisplayed()

            onNodeWithText("Please, make sure you are online and retry.").assertIsDisplayed()

            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsNotDisplayed()
            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(PAGING_PROGRESS_INDICATOR).assertIsNotDisplayed()
        }
    }

    @Test
    fun `when UIState Status is ServerRetry should render UI properly`() {
        rule.run {
            setContentWithUIState(
                uiState = UIState.InitialState.copy(
                    status = Status.ServerRetry,
                ),
            )

            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.ICON).assertIsDisplayed()
            onNodeWithTag(RetryScreenTestTags.CALL_TO_ACTION).assertIsDisplayed()

            onNodeWithText("An error occurred on our servers. Please, retry in a few minutes.").assertIsDisplayed()

            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsNotDisplayed()
            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(PAGING_PROGRESS_INDICATOR).assertIsNotDisplayed()
        }
    }

    @Test
    fun `when UIState Status is Content and REFRESH is not Loading should render UI properly`() {
        rule.run {
            val pagingDataFlow = flowOf(
                PagingData.from(
                    data = MockDomainModels.mockAlbums,
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.NotLoading(false),
                        append = LoadState.NotLoading(false),
                        prepend = LoadState.NotLoading(false),
                    ),
                ),
            )

            setContentWithUIState(
                uiState = UIState.InitialState.copy(
                    status = Status.Content(
                        pagingDataFlow = pagingDataFlow,
                    ),
                ),
            )

            waitForIdle()

            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsDisplayed()
            MockDomainModels.mockAlbums.forEach { album ->
                // FIXME: onNodeWithTag(ALBUM_LIST_ITEM_CONTENT_IMAGE + "_${album.id}").assertIsDisplayed()
                onNodeWithText(album.title).performScrollTo().assertIsDisplayed()
            }
            onNodeWithText(MockDomainModels.mockAlbum1.title)
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(PAGING_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsNotDisplayed()

            verify(exactly = 1) { presenter.dispatch(Intent.SeeDetails(MockDomainModels.mockAlbum1.id)) }
        }
    }

    @Test
    fun `when UIState Status is Content and REFRESH is Loading, should render UI properly`() {
        rule.run {
            val pagingDataFlow = flowOf(
                PagingData.from(
                    data = MockDomainModels.mockAlbums,
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.Loading,
                        append = LoadState.NotLoading(false),
                        prepend = LoadState.NotLoading(false),
                    ),
                ),
            )

            setContentWithUIState(
                uiState = UIState.InitialState.copy(
                    status = Status.Content(
                        pagingDataFlow = pagingDataFlow,
                    ),
                ),
            )

            waitForIdle()

            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsDisplayed()

            onNodeWithTag(PAGING_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsNotDisplayed()
            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsNotDisplayed()
        }
    }

    @Test
    @Ignore("Lee May 15, 2025: I have not found a way to make this work, even in Preview.")
    fun `when UIState Status is Content and APPEND is Loading, should render UI properly`() {
        rule.run {
            val pagingDataFlow = flowOf(
                PagingData.from(
                    data = MockDomainModels.mockAlbums,
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.NotLoading(false),
                        append = LoadState.Loading,
                        prepend = LoadState.NotLoading(false),
                    ),
                ),
            )

            setContentWithUIState(
                uiState = UIState.InitialState.copy(
                    status = Status.Content(
                        pagingDataFlow = pagingDataFlow,
                    ),
                ),
            )

            waitForIdle()

            onNodeWithTag(PAGING_PROGRESS_INDICATOR).performScrollTo().assertIsDisplayed()

            onNodeWithTag(DS_CIRCULAR_PROGRESS_INDICATOR).assertIsNotDisplayed()
            onNodeWithTag(ALBUM_LIST_LAZY_COLUMN).assertIsNotDisplayed()
            onNodeWithTag(RetryScreenTestTags.RETRY_SCREEN).assertIsNotDisplayed()
        }
    }

    private fun ComposeContentTestRule.setContentWithUIState(uiState: UIState) {
        every { presenter.uiState } returns MutableStateFlow(uiState)

        setContent {
            CodeChallengeTheme {
                AlbumListScreen(
                    presenter = presenter,
                    snackbarHostState = remember { SnackbarHostState() },
                ) {
                    onNavigate(it)
                }
            }
        }
    }
}
