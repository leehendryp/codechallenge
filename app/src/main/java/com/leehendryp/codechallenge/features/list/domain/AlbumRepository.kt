package com.leehendryp.codechallenge.features.list.domain

import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAlbums(): Flow<List<Album>>
}
