package com.leehendryp.photoalbum.ui.theme

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAccessibilityManager

@RunWith(RobolectricTestRunner::class)
internal class ContrastLevelTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context
    private lateinit var accessibilityManager: AccessibilityManager
    private lateinit var shadowAccessibilityManager: ShadowAccessibilityManager
    private var contrastLevel: ContrastLevel? = null

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        shadowAccessibilityManager = Shadows.shadowOf(accessibilityManager)
        contrastLevel = null
    }

    @Test
    fun `when accessibility is enabled, should return High contrast`() {
        shadowAccessibilityManager.setEnabled(true)
        context.resources.configuration.fontScale = 1.0f

        setContentAndWaitForIdle()

        assertThat(contrastLevel, equalTo(ContrastLevel.High))
    }

    @Test
    fun `when accessibility is disabled, but font is large, should return Medium contrast`() {
        shadowAccessibilityManager.setEnabled(false)
        context.resources.configuration.fontScale = 1.4f

        setContentAndWaitForIdle()

        assertThat(contrastLevel, equalTo(ContrastLevel.Medium))
    }

    @Test
    fun `when accessibility is disabled and font is not large, should return Default contrast`() {
        shadowAccessibilityManager.setEnabled(false)
        context.resources.configuration.fontScale = 1.0f

        setContentAndWaitForIdle()

        assertThat(contrastLevel, equalTo(ContrastLevel.Default))
    }

    private fun setContentAndWaitForIdle() {
        composeTestRule.setContent {
            contrastLevel = rememberContrastLevel()
        }

        composeTestRule.waitForIdle()
    }
}
