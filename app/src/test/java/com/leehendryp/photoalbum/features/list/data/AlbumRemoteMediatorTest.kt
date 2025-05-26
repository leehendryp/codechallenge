package com.leehendryp.photoalbum.features.list.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.leehendryp.photoalbum.features.common.data.AlbumRemoteMediator
import com.leehendryp.photoalbum.features.common.data.local.LocalDataSource
import com.leehendryp.photoalbum.features.common.data.local.model.AlbumEntity
import com.leehendryp.photoalbum.features.common.data.remote.RemoteDataSource
import com.leehendryp.photoalbum.features.common.domain.MockDomainModels
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalPagingApi
class AlbumRemoteMediatorTest {

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource

    private lateinit var mediator: AlbumRemoteMediator

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mediator = AlbumRemoteMediator(remoteDataSource, localDataSource)
    }

    @Test
    fun `when refresh succeeds, should return Success with proper data`() = runTest {
        coEvery { remoteDataSource.fetchAlbums() } returns flowOf(MockDomainModels.mockAlbums)
        coEvery { localDataSource.save(MockDomainModels.mockAlbums) } just Runs

        val result = mediator.load(
            loadType = LoadType.REFRESH,
            state = emptyPagingState(),
        )

        assertThat(result, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        assertThat(
            (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached,
            `is`(true),
        )
        coVerify(exactly = 1) { remoteDataSource.fetchAlbums() }
        coVerify(exactly = 1) { localDataSource.save(MockDomainModels.mockAlbums) }
    }

    @Test
    fun `when remote fetch fails, should return Error with proper exception`() = runTest {
        val exception = Exception("Some error occurred.")
        coEvery { remoteDataSource.fetchAlbums() } throws exception

        val result = mediator.load(
            loadType = LoadType.REFRESH,
            state = emptyPagingState(),
        )

        assertThat(result, instanceOf(RemoteMediator.MediatorResult.Error::class.java))
        assertThat((result as RemoteMediator.MediatorResult.Error).throwable, equalTo(exception))
        coVerify(exactly = 1) { remoteDataSource.fetchAlbums() }
        coVerify(exactly = 0) { localDataSource.save(any()) }
    }

    @Test
    fun `when it is not REFRESH, should do nothing for APPEND or PREPEND`() = runTest {
        val result = mediator.load(
            loadType = LoadType.APPEND,
            state = emptyPagingState(),
        )

        assertThat(result, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        coVerify(exactly = 0) { remoteDataSource.fetchAlbums() }
        coVerify(exactly = 0) { localDataSource.save(any()) }
    }

    private fun emptyPagingState(): PagingState<Int, AlbumEntity> = PagingState(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(20),
        leadingPlaceholderCount = 0,
    )
}
