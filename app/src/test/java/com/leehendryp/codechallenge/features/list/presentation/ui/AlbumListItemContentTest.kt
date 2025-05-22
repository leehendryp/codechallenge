package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListItemContentTestTags.ALBUM_LIST_ITEM_CONTENT_IMAGE
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListItemContentTestTags.ALBUM_LIST_ITEM_CONTENT_ROW
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AlbumListItemContentTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `when model is provided should render item properly`() {
        val onSelect: () -> Unit = mockk(relaxed = true)

        rule.run {
            setContent {
                CodeChallengeTheme {
                    AlbumListItemContent(model = MockDomainModels.mockAlbum1) {
                        onSelect()
                    }
                }
            }

            onNodeWithText(MockDomainModels.mockAlbum1.title).assertIsDisplayed()
            onNodeWithTag(ALBUM_LIST_ITEM_CONTENT_IMAGE + "_${MockDomainModels.mockAlbum1.id}")
            onNodeWithTag(ALBUM_LIST_ITEM_CONTENT_ROW)
                .assertIsDisplayed()
                .performClick()
                .assertIsEnabled()
                .assertIsDisplayed()

            verify(exactly = 1) { onSelect() }
        }
    }
}
