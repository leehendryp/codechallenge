package com.leehendryp.photoalbum.ui.theme

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

internal enum class ContrastLevel {
    Default,
    Medium,
    High,
}

@Composable
internal fun rememberContrastLevel(): ContrastLevel {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val contrastLevel = remember { mutableStateOf(ContrastLevel.Default) }

    DisposableEffect(context, configuration.fontScale) {
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        val listener = AccessibilityManager.AccessibilityStateChangeListener {
            contrastLevel.value =
                getContrastLevel(accessibilityManager.isEnabled, configuration.fontScale)
        }

        contrastLevel.value =
            getContrastLevel(accessibilityManager.isEnabled, configuration.fontScale)

        accessibilityManager.addAccessibilityStateChangeListener(listener)

        onDispose {
            accessibilityManager.removeAccessibilityStateChangeListener(listener)
        }
    }

    return contrastLevel.value
}

private fun getContrastLevel(isHighContrastEnabled: Boolean, fontScale: Float): ContrastLevel = when {
    isHighContrastEnabled -> ContrastLevel.High
    fontScale > 1.3f -> ContrastLevel.Medium
    else -> ContrastLevel.Default
}
