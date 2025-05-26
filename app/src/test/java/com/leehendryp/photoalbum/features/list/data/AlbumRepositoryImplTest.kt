package com.leehendryp.photoalbum.features.list.data

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.leehendryp.photoalbum.core.domain.DomainException.ClientException
import com.leehendryp.photoalbum.core.domain.RETRIEVAL_ERROR
import com.leehendryp.photoalbum.core.utils.EXCEPTION_FAILURE
import com.leehendryp.photoalbum.core.utils.MainCoroutineRule
import com.leehendryp.photoalbum.features.common.data.AlbumRepositoryImpl
import com.leehendryp.photoalbum.features.common.data.local.LocalDataSource
import com.leehendryp.photoalbum.features.common.data.remote.RemoteDataSource
import com.leehendryp.photoalbum.features.common.domain.Album
import com.leehendryp.photoalbum.features.common.domain.MockDomainModels
import com.leehendryp.photoalbum.features.list.data.model.MockDataModels
import com.leehendryp.photoalbum.features.utils.AlbumDiffCallback
import com.leehendryp.photoalbum.features.utils.ListUpdateCallback
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AlbumRepositoryImplTest {

    @get:Rule
    internal val coroutineRule = MainCoroutineRule()
    private lateinit var repository: AlbumRepositoryImpl

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource

    private val pagingSource = MockDataModels.mockEntities.asPagingSourceFactory().invoke()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        repository = AlbumRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `when local source has data, but remote does not, should emit just local data`() = runTest {
        coEvery { localDataSource.getPagedAlbums() } returns pagingSource
        coEvery { localDataSource.save(any()) } returns Unit
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(emptyList())

        val result = repository.getAlbums().asSnapshot()

        coVerify(exactly = 1) { localDataSource.getPagedAlbums() }
        coVerify(exactly = 1) { localDataSource.save(any()) }
        coVerify(exactly = 1) { remoteDataSource.fetchAlbums() }
        assertThat(result, equalTo(MockDomainModels.mockAlbums))
    }

    @Test
    fun `when local and remote sources have same data, should emit just local data`() = runTest {
        coEvery { localDataSource.getPagedAlbums() } returns pagingSource
        coEvery { localDataSource.save(any()) } returns Unit
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(MockDomainModels.mockAlbums)

        val result = repository.getAlbums().asSnapshot()

        coVerify(exactly = 1) { localDataSource.getPagedAlbums() }
        coVerify(exactly = 1) { localDataSource.save(any()) }
        coVerify(exactly = 1) { remoteDataSource.fetchAlbums() }
        assertThat(result, equalTo(MockDomainModels.mockAlbums))
    }

    @Test
    @Ignore("Lee May 13, 2025: I have found no true-to-life way to test this with Paging3 API at unit test level.")
    fun `when local and remote sources differ, should emit stale then fresh data`() = runTest {
        val stale = MockDataModels.mockEntities.drop(2).asPagingSourceFactory().invoke()
        val fresh = MockDataModels.mockEntities.asPagingSourceFactory().invoke()
        every { localDataSource.getPagedAlbums() } returnsMany listOf(stale, fresh)
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(MockDomainModels.mockAlbums)

        // Lee: Simulates Room causing PagingSource invalidation after saving new data, as it would.
        // The goal is to cause fresh data provision.
        coEvery { localDataSource.save(any()) } answers {
            stale.invalidate()
        }

        val expected = listOf(
            MockDomainModels.mockAlbums.drop(2),
            MockDomainModels.mockAlbums,
        )
        val result = mutableListOf<List<Album>>()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AlbumDiffCallback,
            updateCallback = ListUpdateCallback,
        )

        differ.addOnPagesUpdatedListener {
            result.add(differ.snapshot().items)
        }

        val job = launch {
            repository.getAlbums().take(expected.size).collect { pagingData ->
                differ.submitData(pagingData)
                advanceUntilIdle()
            }
        }

        advanceUntilIdle()

        assertThat(result, equalTo(expected))
        job.cancel()
    }

    @Test
    fun `when remote data source fails it should duly propagate the exception`() = runTest {
        val message = "Error"
        val cause = Throwable()
        coEvery { remoteDataSource.fetchAlbums() } returns
            flow { throw ClientException(message, cause) }
        coEvery { localDataSource.getPagedAlbums() } returns pagingSource

        try {
            repository.getAlbums().asSnapshot()
            fail(EXCEPTION_FAILURE)
        } catch (exception: Throwable) {
            coVerify(exactly = 1) { remoteDataSource.fetchAlbums() }
            coVerify(exactly = 1) { localDataSource.getPagedAlbums() }
            assertThat(exception, instanceOf(ClientException::class.java))
            assertThat(exception.message, equalTo(message))
            assertThat(exception.cause?.cause, equalTo(cause))
        }
    }

    @Test
    fun `when local data source fails it should duly propagate the exception`() = runTest {
        val cause = IllegalAccessException()
        coEvery { localDataSource.getPagedAlbums() } throws ClientException(RETRIEVAL_ERROR, cause)

        try {
            repository.getAlbums().asSnapshot()
            fail(EXCEPTION_FAILURE)
        } catch (exception: Throwable) {
            coVerify(exactly = 0) { remoteDataSource.fetchAlbums() }
            coVerify(exactly = 1) { localDataSource.getPagedAlbums() }
            assertThat(exception, instanceOf(ClientException::class.java))
            assertThat(exception.message, equalTo(RETRIEVAL_ERROR))
            assertThat(exception.cause?.cause, equalTo(cause))
        }
    }
}
