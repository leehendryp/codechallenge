package com.leehendryp.codechallenge.features.list.data.model

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
}
