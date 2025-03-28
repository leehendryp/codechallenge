package com.leehendryp.codechallenge.features.list.domain

internal object MockDomainModels {
    val mockAlbum1: Album = Album(
        id = 1,
        albumId = 10,
        title = "Lorem Ipsum 1",
        url = "url1.com",
        thumbnailUrl = "thumbnailUrl1.com",
    )

    private val mockAlbum2 = Album(
        id = 2,
        albumId = 20,
        title = "Lorem Ipsum 2",
        url = "url2.com",
        thumbnailUrl = "thumbnailUrl2.com",
    )

    private val mockAlbum3 = Album(
        id = 3,
        albumId = 30,
        title = "Lorem Ipsum 3",
        url = "url3.com",
        thumbnailUrl = "thumbnailUrl3.com",
    )

    val mockAlbums = listOf(mockAlbum1, mockAlbum2, mockAlbum3)
}
