package com.leehendryp.codechallenge.features.list.data.remote

import com.leehendryp.codechallenge.core.domain.CodeChallengeException.ClientException
import com.leehendryp.codechallenge.core.domain.CodeChallengeException.ServerException
import com.leehendryp.codechallenge.core.domain.CodeChallengeException.UnknownException
import com.leehendryp.codechallenge.core.utils.EXCEPTION_FAILURE
import com.leehendryp.codechallenge.core.utils.MainCoroutineRule
import com.leehendryp.codechallenge.core.utils.createMockKtorClient
import com.leehendryp.codechallenge.core.utils.loadJsonFromFile
import com.leehendryp.codechallenge.features.common.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.common.data.remote.RemoteDataSourceImpl
import com.leehendryp.codechallenge.features.common.domain.Album
import com.leehendryp.codechallenge.features.common.domain.MockDomainModels
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

@ExperimentalCoroutinesApi
internal class RemoteDataSourceImplTest {

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
            assertThat(e.message, equalTo("A client HTTP error occurred: 400 - Bad Request."))
        }

        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(responseStatus = HttpStatusCode.NotFound),
        )

        try {
            dataSource.fetchAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (e: ClientException) {
            assertThat(e.message, equalTo("A client HTTP error occurred: 404 - Not Found."))
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
            assertThat(
                e.message,
                equalTo("A server HTTP error occurred: 500 - Internal Server Error."),
            )
        }

        dataSource = RemoteDataSourceImpl(
            client = createMockKtorClient(responseStatus = HttpStatusCode.ServiceUnavailable),
        )

        try {
            dataSource.fetchAlbums().first()
            fail(EXCEPTION_FAILURE)
        } catch (e: ServerException) {
            assertThat(
                e.message,
                equalTo("A server HTTP error occurred: 503 - Service Unavailable."),
            )
        }
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
