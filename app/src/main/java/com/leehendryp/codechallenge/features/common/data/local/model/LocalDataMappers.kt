package com.leehendryp.codechallenge.features.common.data.local.model

import com.leehendryp.codechallenge.features.common.domain.Album

internal fun AlbumEntity.toDomainModel(): Album = let { (id, albumId, title, url, thumbnailUrl) ->
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
