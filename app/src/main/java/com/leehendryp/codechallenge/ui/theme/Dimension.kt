package com.leehendryp.codechallenge.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

internal const val DIMENS_TEST_TAG = "DIMENS_TEST_TAG"

@Immutable
internal data class Dimens(
    val spacing: Spacing,
    val strokes: StrokeWidths,
)

@Immutable
internal data class Spacing(
    val xxs: Dp,
    val xs: Dp,
    val s: Dp,
    val m: Dp,
    val l: Dp,
    val xl: Dp,
    val xxl: Dp,
    val xxxl: Dp,
)

@Immutable
internal data class StrokeWidths(val hairline: Dp, val thin: Dp, val thick: Dp)

@Immutable
internal enum class DimensionType {
    Compact,
    Medium,
    Expanded,
}

@Immutable
internal object Dimensions {

    val Compact = Dimens(
        spacing = Spacing(
            xxs = 2.dp,
            xs = 4.dp,
            s = 6.dp,
            m = 8.dp,
            l = 12.dp,
            xl = 16.dp,
            xxl = 24.dp,
            xxxl = 32.dp,
        ),
        strokes = StrokeWidths(hairline = 0.5.dp, thin = 1.dp, thick = 2.dp),
    )

    val Medium = Dimens(
        spacing = Spacing(
            xxs = 2.dp,
            xs = 4.dp,
            s = 8.dp,
            m = 12.dp,
            l = 16.dp,
            xl = 24.dp,
            xxl = 32.dp,
            xxxl = 40.dp,
        ),
        strokes = StrokeWidths(hairline = 1.dp, thin = 2.dp, thick = 4.dp),
    )

    val Expanded = Dimens(
        spacing = Spacing(
            xxs = 4.dp,
            xs = 8.dp,
            s = 12.dp,
            m = 16.dp,
            l = 24.dp,
            xl = 32.dp,
            xxl = 40.dp,
            xxxl = 48.dp,
        ),
        strokes = StrokeWidths(hairline = 1.dp, thin = 3.dp, thick = 6.dp),
    )
}

@Composable
internal fun rememberDimens(): Dimens {
    val windowSize: IntSize = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current

    return remember(windowSize, density) {
        val widthDp = with(density) { windowSize.width.toDp() }
        val type = when {
            widthDp < 360.dp -> DimensionType.Compact
            widthDp < 600.dp -> DimensionType.Medium
            else -> DimensionType.Expanded
        }
        getDimens(type)
    }
}

private fun getDimens(type: DimensionType): Dimens = when (type) {
    DimensionType.Compact -> Dimensions.Compact
    DimensionType.Medium -> Dimensions.Medium
    DimensionType.Expanded -> Dimensions.Expanded
}

internal val LocalDimens = staticCompositionLocalOf<Dimens> { error("No Dimens provided") }
