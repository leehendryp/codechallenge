package com.leehendryp.codechallenge.features.list.data.local

import com.leehendryp.codechallenge.core.domain.LocalInsertionException
import com.leehendryp.codechallenge.core.domain.LocalRetrievalException
import com.leehendryp.codechallenge.core.utils.EXCEPTION_FAILURE
import com.leehendryp.codechallenge.core.utils.MainCoroutineRule
import com.leehendryp.codechallenge.features.list.data.model.MockDataModels
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
    fun `when getting local data finds items, should emit it`() = runTest {
        coEvery { dao.getAll() } returns flowOf(MockDataModels.mockEntities)

        val result = dataSource.getAlbums().first()

        coVerify(exactly = 1) { dataSource.getAlbums() }
        coVerify(exactly = 1) { dao.getAll() }
        assertThat(result, equalTo(MockDomainModels.mockAlbums))
    }

    @Test
    fun `when getting local data finds no items, should emit empty list`() = runTest {
        coEvery { dao.getAll() } returns flowOf(emptyList())

        val result = dataSource.getAlbums().first()

        coVerify(exactly = 1) { dataSource.getAlbums() }
        coVerify(exactly = 1) { dao.getAll() }
        assertThat(result, equalTo(emptyList()))
    }

    @Test
    fun `when data is successfully saved, should emit complete`() = runTest {
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
    fun `when getting local data results in an exception, should catch it`() = runTest {
        coEvery { dao.getAll() } returns flow { throw IllegalArgumentException() }

        try {
            dataSource.getAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (exception: Throwable) {
            coVerify(exactly = 1) { dataSource.getAlbums() }
            coVerify(exactly = 1) { dao.getAll() }
            assertThat(exception, instanceOf(LocalRetrievalException::class.java))
            assertThat(exception.message, equalTo(RETRIEVAL_ERROR))
            assertThat(exception.cause?.cause, instanceOf(IllegalArgumentException::class.java))
        }
    }

    @Test
    fun `when saving local data results in an exception, should catch it`() = runTest {
        coEvery { dao.insertAll(any()) } throws IllegalArgumentException()

        try {
            dataSource.save(MockDomainModels.mockAlbums)
            fail(EXCEPTION_FAILURE)
        } catch (exception: Throwable) {
            coVerify(exactly = 1) { dataSource.save(MockDomainModels.mockAlbums) }
            coVerify(exactly = 1) { dao.insertAll(MockDataModels.mockEntities) }
            assertThat(exception, instanceOf(LocalInsertionException::class.java))
            assertThat(exception.message, equalTo(INSERTION_ERROR))
            assertThat(exception.cause, instanceOf(IllegalArgumentException::class.java))
        }
    }
}
