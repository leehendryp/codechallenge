package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumContentTestTags.ALBUM_CONTENT_IMAGE
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlbumContentTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `when model is provided should render item properly`() {
        rule.setContent {
            CodeChallengeTheme {
                AlbumContent(model = MockDomainModels.mockAlbum1)
            }
        }

        rule.onNodeWithText(MockDomainModels.mockAlbum1.title).assertIsDisplayed()
        rule.onNodeWithTag(ALBUM_CONTENT_IMAGE + "_${MockDomainModels.mockAlbum1.id}")
            .assertIsDisplayed()
    }
}
