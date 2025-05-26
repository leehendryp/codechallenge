package com.leehendryp.photoalbum.features.common.data.remote.model

import com.leehendryp.photoalbum.features.common.domain.Album

internal fun List<AlbumResponse>.toDomainModels() = map { (id, albumId, title, url, thumbnailUrl) ->
    Album(
        id = id ?: -1,
        albumId = albumId ?: -1,
        title = title.orEmpty(),
        url = url.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
    )
}
