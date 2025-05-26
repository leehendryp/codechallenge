package com.leehendryp.photoalbum.features.common.domain

internal data class Album(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)
