package com.leehendryp.codechallenge.features.list.data

import com.leehendryp.codechallenge.features.list.data.local.LocalDataSource
import com.leehendryp.codechallenge.features.list.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.AlbumRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class AlbumRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AlbumRepository {

    /* Lee Apr 9, 2025:
    Chaining onStart to getAll() would be another way of sending the local data first and then updating
    it with our recently persisted remote data, but tightly dependent on getAll() and less explicit.
     */
    override fun getAlbums(): Flow<List<Album>> = channelFlow {
        launch { fetchAndPersist() }

        localDataSource.getAlbums().collect { local ->
            send(local)
        }
    }
        .distinctUntilChanged()
        .flowOn(dispatcher)

    private suspend fun fetchAndPersist() {
        remoteDataSource.fetchAlbums()
            .firstOrNull()?.let { remote ->
                localDataSource.save(remote)
            }
    }
}
