package com.leehendryp.codechallenge.ui.theme

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAccessibilityManager

@RunWith(RobolectricTestRunner::class)
internal class ContrastLevelTest {

    private lateinit var context: Context
    private lateinit var accessibilityManager: AccessibilityManager
    private lateinit var shadowAccessibilityManager: ShadowAccessibilityManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        shadowAccessibilityManager = Shadows.shadowOf(accessibilityManager)
    }

    @Test
    fun `when accessibility is off and font size is normal should return Default`() {
        val shadowAccessibilityManager = Shadows.shadowOf(accessibilityManager)
        shadowAccessibilityManager.setEnabled(false)
        context.resources.configuration.fontScale = 1.0f

        val result = getContrastLevel(context)

        assertThat(result, equalTo(ContrastLevel.Default))
    }

    @Test
    fun `when font size is large should return Medium`() {
        shadowAccessibilityManager.setEnabled(false)
        context.resources.configuration.fontScale = 1.5f

        val result = getContrastLevel(context)

        assertThat(result, equalTo(ContrastLevel.Medium))
    }

    @Test
    fun `when high contrast mode is enabled should return High`() {
        shadowAccessibilityManager.setEnabled(true)

        val result = getContrastLevel(context)

        assertThat(result, equalTo(ContrastLevel.High))
    }
}
