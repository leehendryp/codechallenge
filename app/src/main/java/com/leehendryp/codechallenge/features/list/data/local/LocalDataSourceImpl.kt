package com.leehendryp.codechallenge.features.list.data.local

import com.leehendryp.codechallenge.core.domain.LocalInsertionException
import com.leehendryp.codechallenge.core.domain.LocalRetrievalException
import com.leehendryp.codechallenge.features.list.data.local.model.toDomainModels
import com.leehendryp.codechallenge.features.list.data.local.model.toLocalDataModels
import com.leehendryp.codechallenge.features.list.domain.Album
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal const val RETRIEVAL_ERROR = "An error occurred when getting local data."
internal const val INSERTION_ERROR = "An error occurred when saving local data."

// Lee Apr 15, 2025: Although Room runs on IO by default, the dispatcher is applied here as a fallback.
internal class LocalDataSourceImpl @Inject constructor(
    private val dao: AlbumDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : LocalDataSource {

    override fun getAlbums(): Flow<List<Album>> = dao.getAll()
        .map { entities -> entities.toDomainModels() }
        .catch { cause -> throw LocalRetrievalException(RETRIEVAL_ERROR, cause) }
        .flowOn(dispatcher)

    override suspend fun save(albums: List<Album>) {
        try {
            dao.insertAll(albums.toLocalDataModels())
        } catch (cause: Throwable) {
            throw LocalInsertionException(INSERTION_ERROR, cause)
        }
    }
}
