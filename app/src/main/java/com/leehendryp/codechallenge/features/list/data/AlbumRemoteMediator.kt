package com.leehendryp.codechallenge.features.list.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.leehendryp.codechallenge.features.list.data.local.LocalDataSource
import com.leehendryp.codechallenge.features.list.data.local.model.AlbumEntity
import com.leehendryp.codechallenge.features.list.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.firstOrNull

@OptIn(ExperimentalPagingApi::class)
internal class AlbumRemoteMediator(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : RemoteMediator<Int, AlbumEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AlbumEntity>,
    ): MediatorResult = try {
        if (loadType == LoadType.REFRESH) fetchAndPersist()
        MediatorResult.Success(endOfPaginationReached = true)
    } catch (cause: Throwable) {
        MediatorResult.Error(cause)
    }

    private suspend fun fetchAndPersist() {
        val remoteAlbums = remoteDataSource.fetchAlbums().firstOrNull().orEmpty()
        localDataSource.save(remoteAlbums)
    }
}
