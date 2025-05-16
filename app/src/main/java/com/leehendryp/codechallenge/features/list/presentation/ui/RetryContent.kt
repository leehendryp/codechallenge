package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.leehendryp.codechallenge.R
import com.leehendryp.codechallenge.features.list.presentation.ui.RetryScreenTestTags.CALL_TO_ACTION
import com.leehendryp.codechallenge.features.list.presentation.ui.RetryScreenTestTags.ICON
import com.leehendryp.codechallenge.features.list.presentation.ui.RetryScreenTestTags.RETRY_SCREEN
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.LocalDimens
import com.leehendryp.codechallenge.ui.theme.ThemePreviews

@Composable
internal fun RetryContent(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    @StringRes message: Int,
    hasConnection: Boolean,
    onRetry: () -> Unit,
) {
    val spacing = LocalDimens.current.spacing

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(RETRY_SCREEN),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .padding(spacing.s)
                .size(spacing.xxxl)
                .testTag(ICON),
            imageVector = icon,
            contentDescription = null,
        )
        Text(text = stringResource(message))
        Spacer(Modifier.height(spacing.l))
        Button(
            modifier = Modifier.testTag(CALL_TO_ACTION),
            enabled = hasConnection,
            onClick = onRetry,
        ) {
            Text(stringResource(R.string.feed_retry_call_to_action))
        }
    }
}

@ThemePreviews
@Composable
private fun AlbumListContentEnabledPreview() {
    CodeChallengeTheme {
        Surface {
            RetryContent(
                icon = Icons.Default.DataArray,
                message = R.string.feed_empty,
                hasConnection = true,
            ) {}
        }
    }
}

@ThemePreviews
@Composable
private fun AlbumListContentDisabledPreview() {
    CodeChallengeTheme {
        Surface {
            RetryContent(
                icon = Icons.Default.DataArray,
                message = R.string.feed_empty,
                hasConnection = false,
            ) {}
        }
    }
}

internal object RetryScreenTestTags {
    const val ICON = "ICON"
    const val CALL_TO_ACTION = "CALL_TO_ACTION"
    const val RETRY_SCREEN = "RETRY_SCREEN"
}
