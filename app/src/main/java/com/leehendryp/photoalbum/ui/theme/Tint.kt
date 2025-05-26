package com.leehendryp.photoalbum.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
internal data class TintTheme(
    val iconTint: Color = Color.Unspecified,
)

internal val LocalTintTheme = staticCompositionLocalOf { TintTheme() }
