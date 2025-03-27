package com.leehendryp.codechallenge.ui.theme

import android.content.Context
import android.view.accessibility.AccessibilityManager

internal enum class ContrastLevel {
    Default,
    Medium,
    High,
}

internal fun getContrastLevel(context: Context): ContrastLevel {
    val accessibilityManager =
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val isHighContrastEnabled = accessibilityManager.isEnabled
    val configuration = context.resources.configuration
    val isFontLarge = configuration.fontScale > 1.3

    return when {
        isHighContrastEnabled -> ContrastLevel.High
        isFontLarge -> ContrastLevel.Medium
        else -> ContrastLevel.Default
    }
}
