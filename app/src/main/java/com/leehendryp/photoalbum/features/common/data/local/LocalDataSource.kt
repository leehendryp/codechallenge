package com.leehendryp.photoalbum.features.common.data.local

import androidx.paging.PagingSource
import com.leehendryp.photoalbum.features.common.data.local.model.AlbumEntity
import com.leehendryp.photoalbum.features.common.domain.Album

internal interface LocalDataSource {
    fun getPagedAlbums(): PagingSource<Int, AlbumEntity>
    suspend fun save(albums: List<Album>)
    suspend fun getAlbum(id: Int): AlbumEntity
}
