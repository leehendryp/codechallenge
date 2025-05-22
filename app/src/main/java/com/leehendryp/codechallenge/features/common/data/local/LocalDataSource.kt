package com.leehendryp.codechallenge.features.common.data.local

import androidx.paging.PagingSource
import com.leehendryp.codechallenge.features.common.data.local.model.AlbumEntity
import com.leehendryp.codechallenge.features.common.domain.Album

internal interface LocalDataSource {
    fun getPagedAlbums(): PagingSource<Int, AlbumEntity>
    suspend fun save(albums: List<Album>)
    suspend fun getAlbum(id: Int): AlbumEntity
}
