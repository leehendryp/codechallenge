package com.leehendryp.codechallenge.features.list.data

import com.leehendryp.codechallenge.core.domain.ClientException
import com.leehendryp.codechallenge.core.utils.MainCoroutineRule
import com.leehendryp.codechallenge.features.list.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AlbumRepositoryImplTest {

    @get:Rule
    internal val coroutineRule = MainCoroutineRule()
    private lateinit var albumRepository: AlbumRepositoryImpl
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        albumRepository = AlbumRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `when getAlbums() succeeds it should duly return the albums`() = runTest {
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(MockDomainModels.mockAlbums)

        val result = albumRepository.getAlbums().first()

        assertThat(result, equalTo(MockDomainModels.mockAlbums))
    }

    @Test
    fun `when getAlbums() succeeds but is empty it should duly return the empty list`() = runTest {
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(emptyList())

        val result = albumRepository.getAlbums().first()

        assertThat(result, equalTo(emptyList()))
    }

    @Test(expected = ClientException::class)
    fun `when getAlbums() fails it should duly propagate the exception`() = runTest {
        coEvery { remoteDataSource.fetchAlbums() } throws ClientException("")

        albumRepository.getAlbums().first()
    }
}
