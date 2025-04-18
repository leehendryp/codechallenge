package com.leehendryp.codechallenge.features.list.data.local

import com.leehendryp.codechallenge.features.list.domain.Album
import kotlinx.coroutines.flow.Flow

internal interface LocalDataSource {
    fun getAlbums(): Flow<List<Album>>
    suspend fun save(albums: List<Album>)
}
