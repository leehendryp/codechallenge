package com.leehendryp.codechallenge.ui.ds

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size

internal const val DS_ASYNC_IMAGE_TEST_TAG = "ASYNC_IMAGE"

@Composable
internal fun DSAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    showCrossfade: Boolean = true,
    contentScale: ContentScale = ContentScale.None,
    contentDescription: String?,
) {
    val imageRequest = createImageRequest(imageUrl, showCrossfade)

    AsyncImage(
        modifier = modifier.testTag(tag = DS_ASYNC_IMAGE_TEST_TAG),
        model = imageRequest,
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}

@Composable
private fun createImageRequest(
    imageUrl: String,
    showCrossFade: Boolean,
): ImageRequest = ImageRequest.Builder(LocalContext.current)
    .data(imageUrl)
    .diskCachePolicy(CachePolicy.ENABLED)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .memoryCacheKey(imageUrl)
    .diskCacheKey(imageUrl)
    .allowHardware(true)
    .size(Size.ORIGINAL)
    .crossfade(showCrossFade)
    .build()
