package com.leehendryp.codechallenge.features.list.data.local.model

import com.leehendryp.codechallenge.features.list.domain.Album

internal fun List<AlbumEntity>.toDomainModels(): List<Album> = map { (id, albumId, title, url, thumbnailUrl) ->
    Album(
        id = id ?: -1,
        albumId = albumId ?: -1,
        title = title.orEmpty(),
        url = url.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
    )
}

internal fun List<Album>.toLocalDataModels(): List<AlbumEntity> = map { (id, albumId, title, url, thumbnailUrl) ->
    AlbumEntity(
        id = id,
        albumId = albumId,
        title = title,
        url = url,
        thumbnailUrl = thumbnailUrl,
    )
}
