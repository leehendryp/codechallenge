package com.leehendryp.photoalbum.ui.ds

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
internal fun DSCircularProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.testTag(DS_CIRCULAR_PROGRESS_INDICATOR),
    )
}

const val DS_CIRCULAR_PROGRESS_INDICATOR = "DS_CIRCULAR_PROGRESS_INDICATOR"
