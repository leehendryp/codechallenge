package com.leehendryp.codechallenge.features.list.data.model

import com.leehendryp.codechallenge.features.common.data.local.model.AlbumEntity
import com.leehendryp.codechallenge.features.common.data.remote.model.AlbumResponse

internal object MockDataModels {
    private val mockAlbumResponse1 = AlbumResponse(
        id = 1,
        albumId = 10,
        title = "Lorem Ipsum 1",
        url = "url1.com",
        thumbnailUrl = "thumbnailUrl1.com",
    )

    private val mockAlbumResponse2 = AlbumResponse(
        id = 2,
        albumId = 20,
        title = "Lorem Ipsum 2",
        url = "url2.com",
        thumbnailUrl = "thumbnailUrl2.com",
    )

    private val mockAlbumResponse3 = AlbumResponse(
        id = 3,
        albumId = 30,
        title = "Lorem Ipsum 3",
        url = "url3.com",
        thumbnailUrl = "thumbnailUrl3.com",
    )

    val mockResponses = listOf(mockAlbumResponse1, mockAlbumResponse2, mockAlbumResponse3)

    private val mockAlbumEntity1 = AlbumEntity(
        id = 1,
        albumId = 10,
        title = "Lorem Ipsum 1",
        url = "url1.com",
        thumbnailUrl = "thumbnailUrl1.com",
    )

    private val mockAlbumEntity2 = AlbumEntity(
        id = 2,
        albumId = 20,
        title = "Lorem Ipsum 2",
        url = "url2.com",
        thumbnailUrl = "thumbnailUrl2.com",
    )

    private val mockAlbumEntity3 = AlbumEntity(
        id = 3,
        albumId = 30,
        title = "Lorem Ipsum 3",
        url = "url3.com",
        thumbnailUrl = "thumbnailUrl3.com",
    )

    val mockEntities = listOf(mockAlbumEntity1, mockAlbumEntity2, mockAlbumEntity3)
}
