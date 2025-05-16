package com.leehendryp.codechallenge.ui.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DimensTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `rememberDimens should return Compact for widthDp below 360`() {
        rule.run {
            setContent {
                DimensTestScreen(width = 359)
            }

            onNodeWithTag(DIMENS_TEST_TAG).assertTextContains(Dimensions.Compact.toString())
        }
    }

    @Test
    fun `rememberDimens should return Medium for widthDp between 360 and 599`() {
        rule.run {
            setContent {
                DimensTestScreen(width = 480)
            }

            onNodeWithTag(DIMENS_TEST_TAG).assertTextContains(Dimensions.Medium.toString())
        }
    }

    @Test
    fun `rememberDimens should return Expanded for widthDp 600 and above`() {
        rule.run {
            setContent {
                DimensTestScreen(width = 720)
            }

            onNodeWithTag(DIMENS_TEST_TAG).assertTextContains(Dimensions.Expanded.toString())
        }
    }

    @Composable
    private fun DimensTestScreen(
        width: Int,
    ) {
        CompositionLocalProvider(
            LocalDensity provides Density(1f),
            LocalWindowInfo provides object : WindowInfo {
                override val containerSize = IntSize(width, 800)
                override val isWindowFocused = true
            },
        ) {
            val dimens = rememberDimens()

            Text(
                text = dimens.toString(),
                modifier = Modifier.testTag(DIMENS_TEST_TAG),
            )
        }
    }
}
