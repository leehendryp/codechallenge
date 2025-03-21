package com.leehendryp.codechallenge.features.feed.data.model

import com.leehendryp.codechallenge.features.feed.domain.Album

internal fun List<AlbumResponse>.toDomainModel() = map { (albumId, id, title, url, thumbnailUrl) ->
    Album(
        id = id ?: -1,
        albumId = albumId ?: -1,
        title = title.orEmpty(),
        url = url.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
    )
}
