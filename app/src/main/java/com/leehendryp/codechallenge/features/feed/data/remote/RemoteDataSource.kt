package com.leehendryp.codechallenge.features.feed.data.remote

import com.leehendryp.codechallenge.features.feed.domain.Album
import kotlinx.coroutines.flow.Flow

internal interface RemoteDataSource {
    fun fetchAlbums(): Flow<List<Album>>
}
