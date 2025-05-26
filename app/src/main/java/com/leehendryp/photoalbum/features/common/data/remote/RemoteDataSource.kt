package com.leehendryp.photoalbum.features.common.data.remote

import com.leehendryp.photoalbum.features.common.domain.Album
import kotlinx.coroutines.flow.Flow

internal interface RemoteDataSource {
    fun fetchAlbums(): Flow<List<Album>>
}
