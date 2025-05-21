package com.leehendryp.codechallenge.ui.ds

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.leehendryp.codechallenge.ui.ds.DSCircularProgressIndicatorTestTags.FULL_SCREEN_PROGRESS_INDICATOR

@Composable
internal fun DSCircularProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.testTag(FULL_SCREEN_PROGRESS_INDICATOR),
    )
}

internal object DSCircularProgressIndicatorTestTags {
    const val FULL_SCREEN_PROGRESS_INDICATOR = "FULL_SCREEN_PROGRESS_INDICATOR"
}
