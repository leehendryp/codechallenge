package com.leehendryp.photoalbum.features.list.data.local

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leehendryp.photoalbum.core.data.database.PhotoAlbumDatabase
import com.leehendryp.photoalbum.core.utils.MainCoroutineRule
import com.leehendryp.photoalbum.features.common.data.local.AlbumDao
import com.leehendryp.photoalbum.features.list.data.model.MockDataModels
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

    private lateinit var database: PhotoAlbumDatabase
    private lateinit var dao: AlbumDao

    // Lee Apr 17, 2025: Room forbids database access on the main thread, but for tests it's needed.
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PhotoAlbumDatabase::class.java,
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

    @Test
    fun `when data has been inserted, getAlbum should return correct entity`() = runTest {
        dao.insertAll(MockDataModels.mockEntities)

        val result = dao.getAlbum(1)

        assertThat(result, equalTo(MockDataModels.mockAlbumEntity1))
    }
}
