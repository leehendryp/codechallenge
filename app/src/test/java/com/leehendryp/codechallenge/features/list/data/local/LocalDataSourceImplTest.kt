package com.leehendryp.codechallenge.features.list.data.local

import androidx.paging.PagingSource
import com.leehendryp.codechallenge.core.domain.CodeChallengeException.ClientException
import com.leehendryp.codechallenge.core.domain.INSERTION_ERROR
import com.leehendryp.codechallenge.core.domain.RETRIEVAL_ERROR
import com.leehendryp.codechallenge.core.utils.EXCEPTION_FAILURE
import com.leehendryp.codechallenge.core.utils.MainCoroutineRule
import com.leehendryp.codechallenge.features.list.data.local.model.AlbumEntity
import com.leehendryp.codechallenge.features.list.data.model.MockDataModels
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceImplTest {

    @get:Rule
    internal val coroutineRule = MainCoroutineRule()
    private lateinit var dao: AlbumDao
    private lateinit var dataSource: LocalDataSourceImpl

    @Before
    fun setUp() {
        dao = mockk()
        dataSource = LocalDataSourceImpl(dao)
    }

    @Test
    fun `when getting data succeeds, should return the proper PagingSource`() = runTest {
        val pagingSource = mockk<PagingSource<Int, AlbumEntity>>()
        coEvery { dao.getPagedAlbums() } returns pagingSource

        val result = dataSource.getPagedAlbums()

        coVerify(exactly = 1) { dataSource.getPagedAlbums() }
        coVerify(exactly = 1) { dao.getPagedAlbums() }
        assertThat(result, equalTo(pagingSource))
    }

    @Test
    fun `when getting data fails, should throw proper ClientException`() = runTest {
        coEvery { dao.getPagedAlbums() } throws IllegalArgumentException()

        try {
            dataSource.getPagedAlbums()
            fail(EXCEPTION_FAILURE)
        } catch (exception: Throwable) {
            coVerify(exactly = 1) { dataSource.getPagedAlbums() }
            coVerify(exactly = 1) { dao.getPagedAlbums() }
            assertThat(exception, instanceOf(ClientException::class.java))
            assertThat(exception.message, equalTo(RETRIEVAL_ERROR))
            assertThat(exception.cause, instanceOf(IllegalArgumentException::class.java))
        }
    }

    @Test
    fun `when saving data succeeds, should complete`() = runTest {
        coEvery { dao.insertAll(MockDataModels.mockEntities) } returns Unit

        dataSource.save(MockDomainModels.mockAlbums)

        coVerify(exactly = 1) { dataSource.save(MockDomainModels.mockAlbums) }
        coVerify(exactly = 1) { dao.insertAll(MockDataModels.mockEntities) }
    }

    @Test
    fun `when saving empty list, should not crash`() = runTest {
        coEvery { dao.insertAll(emptyList()) } returns Unit

        dataSource.save(emptyList())

        coVerify(exactly = 1) { dao.insertAll(emptyList()) }
    }

    @Test
    fun `when saving data fails, should throw proper ClientException`() = runTest {
        coEvery { dao.insertAll(any()) } throws IllegalArgumentException()

        try {
            dataSource.save(MockDomainModels.mockAlbums)
            fail(EXCEPTION_FAILURE)
        } catch (exception: Throwable) {
            coVerify(exactly = 1) { dataSource.save(MockDomainModels.mockAlbums) }
            coVerify(exactly = 1) { dao.insertAll(MockDataModels.mockEntities) }
            assertThat(exception, instanceOf(ClientException::class.java))
            assertThat(exception.message, equalTo(INSERTION_ERROR))
            assertThat(exception.cause, instanceOf(IllegalArgumentException::class.java))
        }
    }
}
