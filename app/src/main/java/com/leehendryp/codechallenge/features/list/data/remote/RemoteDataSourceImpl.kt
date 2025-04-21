package com.leehendryp.codechallenge.features.list.data.remote

import com.leehendryp.codechallenge.core.domain.ClientException
import com.leehendryp.codechallenge.core.domain.NetworkException
import com.leehendryp.codechallenge.core.domain.ServerException
import com.leehendryp.codechallenge.core.domain.UnknownException
import com.leehendryp.codechallenge.features.list.data.remote.model.AlbumResponse
import com.leehendryp.codechallenge.features.list.data.remote.model.toDomainModels
import com.leehendryp.codechallenge.features.list.domain.Album
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

const val ALBUM_PATH = "img/shared/technical-test.json"

internal class RemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RemoteDataSource {

    override fun fetchAlbums(): Flow<List<Album>> = flow {
        val response = client.get {
            url { path(ALBUM_PATH) }
            headers.append(HttpHeaders.UserAgent, "CodeChallenge/1.0")
        }

        if (!response.status.isSuccess()) throw response.toCodeChallengeException()

        val albums = loadJsonIntoMemoryFirst(response)

        emit(albums.toDomainModels())
    }
        .catch { throw it.toCodeChallengeException() }
        .flowOn(dispatcher)

    /*
    Lee Apr 9, 2025: This guarantees that we load the whole JSON first, to avoid an IOException caused
    by an over-3000-line body.
     */
    private suspend fun loadJsonIntoMemoryFirst(response: HttpResponse): List<AlbumResponse> {
        val channel = response.bodyAsChannel()
        val content = channel.readRemaining().readText()
        val responses = Json.decodeFromString<List<AlbumResponse>>(content)
        return responses
    }
}

private fun HttpResponse.toCodeChallengeException(): Exception = when (status.value) {
    in 400..499 -> ClientException("Client error occurred: ${status.value} - ${status.description}.")
    in 500..599 -> ServerException("Server error occurred: ${status.value} - ${status.description}.")
    else -> UnknownException(
        "An unknown error occurred: ${status.value} - ${status.description}.",
        null,
    )
}

private fun Throwable.toCodeChallengeException() = when (this) {
    is IOException -> NetworkException("A network error occurred.", this)
    is ClientException, is ServerException -> this
    else -> UnknownException("An unknown error occurred.", this)
}
