package com.leehendryp.codechallenge.features.list.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.leehendryp.codechallenge.features.list.data.local.LocalDataSource
import com.leehendryp.codechallenge.features.list.data.local.model.toDomainModel
import com.leehendryp.codechallenge.features.list.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.AlbumRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
internal class AlbumRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AlbumRepository {

    override fun getAlbums(): Flow<PagingData<Album>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        remoteMediator = AlbumRemoteMediator(remoteDataSource, localDataSource),
        pagingSourceFactory = { localDataSource.getPagedAlbums() },
    )
        .flow
        .map { pagingData -> pagingData.map { it.toDomainModel() } }
        .distinctUntilChanged()
        .flowOn(dispatcher)

    override fun getAlbum(id: Int): Flow<Album> = flow {
        emit(localDataSource.getAlbum(id).toDomainModel())
    }.flowOn(dispatcher)
}
