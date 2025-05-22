package com.leehendryp.codechallenge.features.details.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.ui.ds.DS_ASYNC_IMAGE_TEST_TAG
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlbumDetailsItemContentTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `when model is provided should render item properly`() {
        rule.run {
            setContent {
                CodeChallengeTheme {
                    AlbumDetailsItemContent(
                        model = MockDomainModels.mockAlbum1,
                    )
                }
            }

            onNodeWithText(MockDomainModels.mockAlbum1.title).assertIsDisplayed()
            onNodeWithTag(DS_ASYNC_IMAGE_TEST_TAG)
        }
    }
}
