package com.leehendryp.codechallenge.features.list.data.local

import androidx.paging.PagingSource
import com.leehendryp.codechallenge.features.list.data.local.model.AlbumEntity
import com.leehendryp.codechallenge.features.list.domain.Album

internal interface LocalDataSource {
    fun getPagedAlbums(): PagingSource<Int, AlbumEntity>
    suspend fun save(albums: List<Album>)
    suspend fun getAlbum(id: Int): AlbumEntity
}
