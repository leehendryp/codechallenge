package com.leehendryp.photoalbum.features.common.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/*
Lee Apr 22, 2025: androidx-paging-common is pure Kotlin, reason why having PagingData as a dependency
should not be a bigger issue in the domain layer.
 */
internal interface AlbumRepository {
    fun getAlbums(): Flow<PagingData<Album>>
    fun getAlbum(id: Int): Flow<Album>
}
