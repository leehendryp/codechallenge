package com.leehendryp.codechallenge.features.list.data.remote

import com.leehendryp.codechallenge.core.domain.ClientException
import com.leehendryp.codechallenge.core.domain.NetworkException
import com.leehendryp.codechallenge.core.domain.ServerException
import com.leehendryp.codechallenge.core.domain.UnknownException
import com.leehendryp.codechallenge.features.list.data.EXCEPTION_FAILURE
import com.leehendryp.codechallenge.features.list.data.MainCoroutineRule
import com.leehendryp.codechallenge.features.list.data.createMockKtorClient
import com.leehendryp.codechallenge.features.list.data.loadJsonFromFile
import com.leehendryp.codechallenge.features.list.data.model.MockDomainModels
import com.leehendryp.codechallenge.features.list.domain.Album
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class RemoteDataSourceImplTest {

    @get:Rule
    internal val coroutineRule = MainCoroutineRule()
    private lateinit var dataSource: RemoteDataSource

    @Test
    fun `when response is successful should duly return the album list`() = runTest {
        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(
                responseContent = loadJsonFromFile("successful_response.json"),
                responseStatus = HttpStatusCode.OK,
            ),
        )

        val result: List<Album> = dataSource.fetchAlbums().first()

        assertThat(result.size, equalTo(3))
        assertThat(result, equalTo(MockDomainModels.mockAlbums))
    }

    @Test
    fun `when error in 4xx should throw ClientException with the exact status code`() = runTest {
        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(responseStatus = HttpStatusCode.BadRequest),
        )

        try {
            dataSource.fetchAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (e: ClientException) {
            assertThat(e.message, equalTo("Client error occurred: 400 - Bad Request."))
        }

        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(responseStatus = HttpStatusCode.NotFound),
        )

        try {
            dataSource.fetchAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (e: ClientException) {
            assertThat(e.message, equalTo("Client error occurred: 404 - Not Found."))
        }
    }

    @Test
    fun `when error in 5xx should throw ClientException with the exact status code`() = runTest {
        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(responseStatus = HttpStatusCode.InternalServerError),
        )

        try {
            dataSource.fetchAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (e: ServerException) {
            assertThat(e.message, equalTo("Server error occurred: 500 - Internal Server Error."))
        }

        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(responseStatus = HttpStatusCode.ServiceUnavailable),
        )

        try {
            dataSource.fetchAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (e: ServerException) {
            assertThat(e.message, equalTo("Server error occurred: 503 - Service Unavailable."))
        }
    }

    @Test(expected = NetworkException::class)
    fun `when an IOException occurs should throw NetworkException`() = runTest {
        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(
                mockEngine = MockEngine { throw IOException() },
            ),
        )

        dataSource.fetchAlbums().first()
    }

    @Test(expected = UnknownException::class)
    fun `when a generic error occurs should throw UnknownException`() = runTest {
        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(
                mockEngine = MockEngine { throw Exception() },
            ),
        )

        dataSource.fetchAlbums().first()
    }

    @Test
    fun `when a ClientException or ServerException occurs should rethrow`() = runTest {
        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(
                mockEngine = MockEngine { throw ClientException("message") },
            ),
        )

        try {
            dataSource.fetchAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (e: ClientException) {
            assertThat(e.message, equalTo("message"))
        }

        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(
                mockEngine = MockEngine { throw ServerException("message") },
            ),
        )

        try {
            dataSource.fetchAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (e: ServerException) {
            assertThat(e.message, equalTo("message"))
        }
    }
}
