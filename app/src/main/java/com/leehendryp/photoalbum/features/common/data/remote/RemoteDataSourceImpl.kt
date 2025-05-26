package com.leehendryp.photoalbum.features.common.data.remote

import com.leehendryp.photoalbum.BuildConfig
import com.leehendryp.photoalbum.core.domain.DomainException.ClientException
import com.leehendryp.photoalbum.core.domain.DomainException.ServerException
import com.leehendryp.photoalbum.core.domain.DomainException.UnknownException
import com.leehendryp.photoalbum.features.common.data.remote.model.AlbumResponse
import com.leehendryp.photoalbum.features.common.data.remote.model.toDomainModels
import com.leehendryp.photoalbum.features.common.domain.Album
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

internal class RemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RemoteDataSource {

    override fun fetchAlbums(): Flow<List<Album>> = flow {
        val response = client.get {
            url { path(BuildConfig.API_PATH) }
            headers.append(HttpHeaders.UserAgent, BuildConfig.API_USER_AGENT)
        }

        if (!response.status.isSuccess()) throw response.toDomainException()

        val albums = loadJsonIntoMemoryFirst(response)

        emit(albums.toDomainModels())
    }
        .catch { throw it.toDomainException() }
        .flowOn(dispatcher)

    /*
    Lee Apr 9, 2025: This guarantees that we load the whole JSON first, to avoid an IOException caused
    by an over-3000-line body.
     */
    private suspend fun loadJsonIntoMemoryFirst(response: HttpResponse): List<AlbumResponse> {
        val channel = response.bodyAsChannel()
        val content = channel.readRemaining().readText()
        val responses = Json.Default.decodeFromString<List<AlbumResponse>>(content)
        return responses
    }
}

private fun HttpResponse.toDomainException(): Exception = when (status.value) {
    in 400..499 -> ClientException("A client HTTP error occurred: ${status.value} - ${status.description}.")
    in 500..599 -> ServerException("A server HTTP error occurred: ${status.value} - ${status.description}.")
    else -> UnknownException("An unknown HTTP error occurred: ${status.value} - ${status.description}.")
}

private fun Throwable.toDomainException(): Exception = when (this) {
    is IOException -> ClientException("A network error occurred.", this)
    is ClientException, is ServerException -> this
    else -> UnknownException("An unknown error occurred.", this)
}
