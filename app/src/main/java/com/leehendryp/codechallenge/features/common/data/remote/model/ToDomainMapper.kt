package com.leehendryp.codechallenge.features.common.data.remote.model

import com.leehendryp.codechallenge.features.common.domain.Album

internal fun List<AlbumResponse>.toDomainModels() = map { (id, albumId, title, url, thumbnailUrl) ->
    Album(
        id = id ?: -1,
        albumId = albumId ?: -1,
        title = title.orEmpty(),
        url = url.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
    )
}
