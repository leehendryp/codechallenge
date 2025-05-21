package com.leehendryp.codechallenge.features.list.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leehendryp.codechallenge.features.list.data.local.model.AlbumEntity

@Dao
interface AlbumDao {
    @Query("SELECT * FROM album ORDER BY title ASC")
    fun getPagedAlbums(): PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE id = :id")
    fun getAlbum(id: Int): AlbumEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)
}
