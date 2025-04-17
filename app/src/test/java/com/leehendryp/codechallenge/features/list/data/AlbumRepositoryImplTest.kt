package com.leehendryp.codechallenge.features.list.data

import com.leehendryp.codechallenge.core.domain.ClientException
import com.leehendryp.codechallenge.core.utils.MainCoroutineRule
import com.leehendryp.codechallenge.features.list.data.local.LocalDataSource
import com.leehendryp.codechallenge.features.list.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        albumRepository = AlbumRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `when local source has data, but remote does not, should emit just local data`() = runTest {
        coEvery { localDataSource.getAlbums() } returns flowOf(MockDomainModels.mockAlbums)
        coEvery { localDataSource.save(any()) } returns Unit
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(emptyList())

        val result = albumRepository.getAlbums().toList()

        coVerify(exactly = 1) { localDataSource.getAlbums() }
        coVerify(exactly = 1) { localDataSource.save(any()) }
        coVerify(exactly = 1) { remoteDataSource.fetchAlbums() }
        assertThat(result, equalTo(listOf(MockDomainModels.mockAlbums)))
    }

    @Test
    fun `when local and remote sources have same data, should emit just local data`() = runTest {
        coEvery { localDataSource.getAlbums() } returns flowOf(MockDomainModels.mockAlbums)
        coEvery { localDataSource.save(any()) } returns Unit
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(MockDomainModels.mockAlbums)

        val result = albumRepository.getAlbums().toList()

        coVerify(exactly = 1) { localDataSource.getAlbums() }
        coVerify(exactly = 1) { localDataSource.save(any()) }
        coVerify(exactly = 1) { remoteDataSource.fetchAlbums() }
        assertThat(result, equalTo(listOf(MockDomainModels.mockAlbums)))
    }

    @Test
    fun `when local and remote sources have different data, should emit local, save remote and re-emit local`() = runTest {
        coEvery { localDataSource.getAlbums() } returns flow {
            emit(MockDomainModels.mockAlbums.drop(2))
            emit(MockDomainModels.mockAlbums)
        }
        coEvery { localDataSource.save(any()) } returns Unit
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(MockDomainModels.mockAlbums)

        val result = albumRepository.getAlbums().toList()

        coVerify(exactly = 1) { localDataSource.getAlbums() }
        coVerify(exactly = 1) { localDataSource.save(any()) }
        coVerify(exactly = 1) { remoteDataSource.fetchAlbums() }
        assertThat(
            result,
            equalTo(listOf(MockDomainModels.mockAlbums.drop(2), MockDomainModels.mockAlbums)),
        )
    }

    @Test(expected = ClientException::class)
    fun `when getAlbums() fails it should duly propagate the exception`() = runTest {
        coEvery { remoteDataSource.fetchAlbums() } throws ClientException("")

        albumRepository.getAlbums().first()
    }
}
