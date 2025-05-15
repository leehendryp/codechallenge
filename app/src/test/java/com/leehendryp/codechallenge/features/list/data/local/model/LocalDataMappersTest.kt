package com.leehendryp.codechallenge.features.list.data.local.model

import com.leehendryp.codechallenge.features.list.data.model.MockDataModels
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class LocalDataMappersTest {

    @Test
    fun `when entity values are not null toDomainModel should map values correctly`() {
        val result = MockDataModels.mockEntities.map { it.toDomainModel() }

        MatcherAssert.assertThat(result, equalTo(MockDomainModels.mockAlbums))
    }

    @Test
    fun `when entity values are null toDomainModel should map values correctly`() {
        val entities = listOf(
            AlbumEntity(albumId = null, id = null, title = null, url = null, thumbnailUrl = null),
        )
        val expected = listOf(
            Album(id = -1, albumId = -1, title = "", url = "", thumbnailUrl = ""),
        )

        val result = entities.map { it.toDomainModel() }

        MatcherAssert.assertThat(result, equalTo(expected))
    }

    @Test
    fun `when domain model values are not null toLocalDataModels should map values correctly`() {
        val result = MockDomainModels.mockAlbums.toLocalDataModels()

        MatcherAssert.assertThat(result, equalTo(MockDataModels.mockEntities))
    }

    @Test
    fun `when domain model values are null toLocalDataModels should map values correctly`() {
        val albums = listOf(
            Album(id = -1, albumId = -1, title = "", url = "", thumbnailUrl = ""),
        )
        val expected = listOf(
            AlbumEntity(albumId = -1, id = -1, title = "", url = "", thumbnailUrl = ""),
        )

        val result = albums.toLocalDataModels()

        MatcherAssert.assertThat(result, equalTo(expected))
    }
}
