package com.leehendryp.codechallenge.ui.theme

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ThemeTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun typographyCodeChallengeTypography() {
        rule.setContent {
            CodeChallengeTheme(
                contrastLevel = ContrastLevel.Default,
                isDarkTheme = false,
                enableDynamicTheming = false,
            ) {
                assertThat(LocalTypography.current, equalTo(MaterialTheme.typography))
            }
        }
    }

    @Test
    fun contrastLevelDefault_isDarkThemeFalse_enableDynamicThemingFalse() {
        rule.setContent {
            CodeChallengeTheme(
                contrastLevel = ContrastLevel.Default,
                isDarkTheme = false,
                enableDynamicTheming = false,
            ) {
                val colorScheme = lightColorScheme
                assertThatColorSchemeEqualTo(colorScheme, MaterialTheme.colorScheme)
                assertThat(LocalGradientColors.current, equalTo(gradientColors(colorScheme)))
                assertThat(LocalBackgroundTheme.current, equalTo(backgroundTheme(colorScheme)))
                assertThat(LocalTintTheme.current, equalTo(TintTheme()))
            }
        }
    }

    @Test
    fun contrastLevelMedium_isDarkThemeFalse_enableDynamicThemingFalse() {
        rule.setContent {
            CodeChallengeTheme(
                contrastLevel = ContrastLevel.Medium,
                isDarkTheme = false,
                enableDynamicTheming = false,
            ) {
                val colorScheme = mediumContrastLightColorScheme
                assertThatColorSchemeEqualTo(colorScheme, MaterialTheme.colorScheme)
                assertThat(LocalGradientColors.current, equalTo(gradientColors(colorScheme)))
                assertThat(LocalBackgroundTheme.current, equalTo(backgroundTheme(colorScheme)))
                assertThat(LocalTintTheme.current, equalTo(TintTheme()))
            }
        }
    }

    @Test
    fun contrastLevelHigh_isDarkThemeFalse_enableDynamicThemingFalse() {
        rule.setContent {
            CodeChallengeTheme(
                contrastLevel = ContrastLevel.High,
                isDarkTheme = false,
                enableDynamicTheming = false,
            ) {
                val colorScheme = highContrastLightColorScheme
                assertThatColorSchemeEqualTo(colorScheme, MaterialTheme.colorScheme)
                assertThat(LocalGradientColors.current, equalTo(gradientColors(colorScheme)))
                assertThat(LocalBackgroundTheme.current, equalTo(backgroundTheme(colorScheme)))
                assertThat(LocalTintTheme.current, equalTo(TintTheme()))
            }
        }
    }

    @Test
    fun contrastLevelDefault_isDarkThemeTrue_enableDynamicThemingFalse() {
        rule.setContent {
            CodeChallengeTheme(
                contrastLevel = ContrastLevel.Default,
                isDarkTheme = true,
                enableDynamicTheming = false,
            ) {
                val colorScheme = darkColorScheme
                assertThatColorSchemeEqualTo(colorScheme, MaterialTheme.colorScheme)
                assertThat(LocalGradientColors.current, equalTo(gradientColors(colorScheme)))
                assertThat(LocalBackgroundTheme.current, equalTo(backgroundTheme(colorScheme)))
                assertThat(LocalTintTheme.current, equalTo(TintTheme()))
            }
        }
    }

    @Test
    fun contrastLevelMedium_isDarkThemeTrue_enableDynamicThemingFalse() {
        rule.setContent {
            CodeChallengeTheme(
                contrastLevel = ContrastLevel.Medium,
                isDarkTheme = true,
                enableDynamicTheming = false,
            ) {
                val colorScheme = mediumContrastDarkColorScheme
                assertThatColorSchemeEqualTo(colorScheme, MaterialTheme.colorScheme)
                assertThat(LocalGradientColors.current, equalTo(gradientColors(colorScheme)))
                assertThat(LocalBackgroundTheme.current, equalTo(backgroundTheme(colorScheme)))
                assertThat(LocalTintTheme.current, equalTo(TintTheme()))
            }
        }
    }

    @Test
    fun contrastLevelHigh_isDarkThemeTrue_enableDynamicThemingFalse() {
        rule.setContent {
            CodeChallengeTheme(
                contrastLevel = ContrastLevel.High,
                isDarkTheme = true,
                enableDynamicTheming = false,
            ) {
                val colorScheme = highContrastDarkColorScheme
                assertThatColorSchemeEqualTo(colorScheme, MaterialTheme.colorScheme)
                assertThat(LocalGradientColors.current, equalTo(gradientColors(colorScheme)))
                assertThat(LocalBackgroundTheme.current, equalTo(backgroundTheme(colorScheme)))
                assertThat(LocalTintTheme.current, equalTo(TintTheme()))
            }
        }
    }

    @Test
    fun contrastLevelDefault_isDarkThemeFalse_enableDynamicThemingTrue() {
        rule.setContent {
            CodeChallengeTheme(
                isDarkTheme = false,
                enableDynamicTheming = true,
            ) {
                val colorScheme = dynamicLightColorSchemeOrDefault()
                assertThatColorSchemeEqualTo(colorScheme, MaterialTheme.colorScheme)
                assertThat(
                    LocalGradientColors.current,
                    equalTo(dynamicGradientColorsOrDefault(colorScheme)),
                )
                assertThat(LocalBackgroundTheme.current, equalTo(backgroundTheme(colorScheme)))
                assertThat(LocalTintTheme.current, equalTo(dynamicTintThemeOrDefault(colorScheme)))
            }
        }
    }

    @Test
    fun contrastLevelDefault_isDarkThemeTrue_enableDynamicThemingTrue() {
        rule.setContent {
            CodeChallengeTheme(
                isDarkTheme = true,
                enableDynamicTheming = true,
            ) {
                val colorScheme = dynamicDarkColorSchemeOrDefault()
                assertThatColorSchemeEqualTo(colorScheme, MaterialTheme.colorScheme)
                assertThat(
                    LocalGradientColors.current,
                    equalTo(dynamicGradientColorsOrDefault(colorScheme)),
                )
                assertThat(LocalBackgroundTheme.current, equalTo(backgroundTheme(colorScheme)))
                assertThat(LocalTintTheme.current, equalTo(dynamicTintThemeOrDefault(colorScheme)))
            }
        }
    }

    private fun dynamicTintThemeOrDefault(colorScheme: ColorScheme): TintTheme = when {
        SDK_INT >= VERSION_CODES.S -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }

    private fun gradientColors(colorScheme: ColorScheme) = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface,
    )

    private fun backgroundTheme(colorScheme: ColorScheme) = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )

    @Composable
    private fun dynamicLightColorSchemeOrDefault(): ColorScheme = when {
        SDK_INT >= VERSION_CODES.S -> dynamicLightColorScheme(LocalContext.current)
        else -> lightColorScheme
    }

    @Composable
    private fun dynamicDarkColorSchemeOrDefault(): ColorScheme = when {
        SDK_INT >= VERSION_CODES.S -> dynamicDarkColorScheme(LocalContext.current)
        else -> darkColorScheme
    }

    private fun dynamicGradientColorsOrDefault(colorScheme: ColorScheme): GradientColors = when {
        SDK_INT >= VERSION_CODES.S -> emptyGradientColors(colorScheme)
        else -> gradientColors(colorScheme)
    }

    private fun emptyGradientColors(colorScheme: ColorScheme) = GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))

    private fun assertThatColorSchemeEqualTo(
        actual: ColorScheme,
        expected: ColorScheme,
    ) {
        assertThat(actual.primary, equalTo(expected.primary))
        assertThat(actual.onPrimary, equalTo(expected.onPrimary))
        assertThat(actual.primaryContainer, equalTo(expected.primaryContainer))
        assertThat(actual.onPrimaryContainer, equalTo(expected.onPrimaryContainer))
        assertThat(actual.secondary, equalTo(expected.secondary))
        assertThat(actual.onSecondary, equalTo(expected.onSecondary))
        assertThat(actual.secondaryContainer, equalTo(expected.secondaryContainer))
        assertThat(actual.onSecondaryContainer, equalTo(expected.onSecondaryContainer))
        assertThat(actual.tertiary, equalTo(expected.tertiary))
        assertThat(actual.onTertiary, equalTo(expected.onTertiary))
        assertThat(actual.tertiaryContainer, equalTo(expected.tertiaryContainer))
        assertThat(actual.onTertiaryContainer, equalTo(expected.onTertiaryContainer))
        assertThat(actual.error, equalTo(expected.error))
        assertThat(actual.onError, equalTo(expected.onError))
        assertThat(actual.errorContainer, equalTo(expected.errorContainer))
        assertThat(actual.onErrorContainer, equalTo(expected.onErrorContainer))
        assertThat(actual.background, equalTo(expected.background))
        assertThat(actual.onBackground, equalTo(expected.onBackground))
        assertThat(actual.surface, equalTo(expected.surface))
        assertThat(actual.onSurface, equalTo(expected.onSurface))
        assertThat(actual.surfaceVariant, equalTo(expected.surfaceVariant))
        assertThat(actual.onSurfaceVariant, equalTo(expected.onSurfaceVariant))
        assertThat(actual.outline, equalTo(expected.outline))
        assertThat(actual.outlineVariant, equalTo(expected.outlineVariant))
        assertThat(actual.scrim, equalTo(expected.scrim))
        assertThat(actual.inverseSurface, equalTo(expected.inverseSurface))
        assertThat(actual.inverseOnSurface, equalTo(expected.inverseOnSurface))
        assertThat(actual.inversePrimary, equalTo(expected.inversePrimary))
        assertThat(actual.surfaceDim, equalTo(expected.surfaceDim))
        assertThat(actual.surfaceBright, equalTo(expected.surfaceBright))
        assertThat(actual.surfaceContainerLowest, equalTo(expected.surfaceContainerLowest))
        assertThat(actual.surfaceContainerLow, equalTo(expected.surfaceContainerLow))
        assertThat(actual.surfaceContainer, equalTo(expected.surfaceContainer))
        assertThat(actual.surfaceContainerHigh, equalTo(expected.surfaceContainerHigh))
        assertThat(actual.surfaceContainerHighest, equalTo(expected.surfaceContainerHighest))
    }
}
