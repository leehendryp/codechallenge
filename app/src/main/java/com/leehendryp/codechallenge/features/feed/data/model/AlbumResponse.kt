package com.leehendryp.codechallenge.features.feed.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumResponse(
    @SerialName("albumId") val albumId: Int?,
    @SerialName("id") val id: Int?,
    @SerialName("title") val title: String?,
    @SerialName("url") val url: String?,
    @SerialName("thumbnailUrl") val thumbnailUrl: String?,
)
