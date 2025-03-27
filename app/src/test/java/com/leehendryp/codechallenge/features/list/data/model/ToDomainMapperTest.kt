package com.leehendryp.codechallenge.features.list.data.model

import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ToDomainMapperTest {

    @Test
    fun `when response values are not null toDomainModel should map values correctly`() {
        val result = MockDataModels.mockResponses.toDomainModel()

        assertThat(result, equalTo(MockDomainModels.mockAlbums))
    }

    @Test
    fun `when response values are null toDomainModel should map values correctly`() {
        val responses = listOf(
            AlbumResponse(albumId = null, id = null, title = null, url = null, thumbnailUrl = null),
        )
        val expected = listOf(
            Album(id = -1, albumId = -1, title = "", url = "", thumbnailUrl = ""),
        )

        val result = responses.toDomainModel()

        assertThat(result, equalTo(expected))
    }
}
