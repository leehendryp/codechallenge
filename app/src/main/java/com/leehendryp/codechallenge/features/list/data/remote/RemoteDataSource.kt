package com.leehendryp.codechallenge.features.list.data.remote

import com.leehendryp.codechallenge.features.list.domain.Album
import kotlinx.coroutines.flow.Flow

internal interface RemoteDataSource {
    fun fetchAlbums(): Flow<List<Album>>
}
