package com.leehendryp.codechallenge.features.common.data.remote

import com.leehendryp.codechallenge.features.common.domain.Album
import kotlinx.coroutines.flow.Flow

internal interface RemoteDataSource {
    fun fetchAlbums(): Flow<List<Album>>
}
