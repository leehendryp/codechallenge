package com.leehendryp.photoalbum.features.details.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.leehendryp.photoalbum.features.common.domain.MockDomainModels
import com.leehendryp.photoalbum.ui.ds.DS_ASYNC_IMAGE_TEST_TAG
import com.leehendryp.photoalbum.ui.theme.PhotoAlbumTheme
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
                PhotoAlbumTheme {
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
