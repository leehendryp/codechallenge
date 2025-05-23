package com.leehendryp.codechallenge.features.list.data.local

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leehendryp.codechallenge.core.data.database.CodeChallengeDatabase
import com.leehendryp.codechallenge.core.utils.MainCoroutineRule
import com.leehendryp.codechallenge.features.common.data.local.AlbumDao
import com.leehendryp.codechallenge.features.list.data.model.MockDataModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AlbumDaoTest {

    @get:Rule
    internal val coroutineRule = MainCoroutineRule()

    private lateinit var database: CodeChallengeDatabase
    private lateinit var dao: AlbumDao

    // Lee Apr 17, 2025: Room forbids database access on the main thread, but for tests it's needed.
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CodeChallengeDatabase::class.java,
        )
            .allowMainThreadQueries()
            .build()

        dao = database.albumDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `when data has been inserted, getPagedAlbums should return correct PagingData`() = runTest {
        dao.insertAll(MockDataModels.mockEntities)

        val expected = MockDataModels.mockEntities.sortedBy { it.title }
        val pagingSource = dao.getPagedAlbums()
            .load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 10,
                    placeholdersEnabled = false,
                ),
            )
        val result = (pagingSource as PagingSource.LoadResult.Page).data

        assertThat(result, equalTo(expected))
    }
}
