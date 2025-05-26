package com.leehendryp.photoalbum.features.common.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leehendryp.photoalbum.features.common.data.local.model.AlbumEntity

@Dao
interface AlbumDao {
    @Query("SELECT * FROM album ORDER BY title ASC")
    fun getPagedAlbums(): PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM album WHERE id = :id")
    fun getAlbum(id: Int): AlbumEntity

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)
}
