package com.leehendryp.codechallenge.features.feed.data

import com.leehendryp.codechallenge.features.feed.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.feed.domain.Album
import com.leehendryp.codechallenge.features.feed.domain.AlbumRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class AlbumRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AlbumRepository {

    override fun getAlbums(): Flow<List<Album>> = remoteDataSource.fetchAlbums().flowOn(dispatcher)
}
