package com.leehendryp.codechallenge.features.common.data.local

import androidx.paging.PagingSource
import com.leehendryp.codechallenge.core.domain.CodeChallengeException
import com.leehendryp.codechallenge.core.domain.INSERTION_ERROR
import com.leehendryp.codechallenge.core.domain.NO_SUCH_ITEM_ERROR
import com.leehendryp.codechallenge.core.domain.RETRIEVAL_ERROR
import com.leehendryp.codechallenge.features.common.data.local.model.AlbumEntity
import com.leehendryp.codechallenge.features.common.data.local.model.toLocalDataModels
import com.leehendryp.codechallenge.features.common.domain.Album
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val dao: AlbumDao,
) : LocalDataSource {

    override fun getPagedAlbums(): PagingSource<Int, AlbumEntity> = try {
        dao.getPagedAlbums()
    } catch (cause: Throwable) {
        throw CodeChallengeException.ClientException(RETRIEVAL_ERROR, cause)
    }

    override suspend fun save(albums: List<Album>) = try {
        dao.insertAll(albums.toLocalDataModels())
    } catch (cause: Throwable) {
        throw CodeChallengeException.ClientException(INSERTION_ERROR, cause)
    }

    override suspend fun getAlbum(id: Int): AlbumEntity = try {
        dao.getAlbum(id)
    } catch (cause: Throwable) {
        throw CodeChallengeException.ClientException(NO_SUCH_ITEM_ERROR, cause)
    }
}
