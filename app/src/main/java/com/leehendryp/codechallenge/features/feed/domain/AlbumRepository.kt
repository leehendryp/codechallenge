package com.leehendryp.codechallenge.features.feed.domain

import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAlbums(): Flow<List<Album>>
}
