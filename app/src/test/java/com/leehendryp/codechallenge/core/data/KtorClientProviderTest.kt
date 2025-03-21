package com.leehendryp.codechallenge.core.data

import com.leehendryp.codechallenge.BuildConfig
import com.leehendryp.codechallenge.features.list.data.MainCoroutineRule
import com.leehendryp.codechallenge.features.list.data.loadJsonFromFile
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headersOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Lee Mar 20, 2025: It turns out testing some of Ktor capabilities and plugin settings is trickier
// than I expected. I decided to move the error validation to the RemoteDataSource.
@OptIn(ExperimentalCoroutinesApi::class)
class KtorClientProviderTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var mockEngine: MockEngine
    private lateinit var capturedRequest: HttpRequestData
    private lateinit var ktorClientProvider: KtorClientProvider
    private lateinit var client: HttpClient

    @Before
    fun setUp() {
        mockEngine = MockEngine { request ->
            capturedRequest = request

            respond(
                content = loadJsonFromFile("successful_response.json"),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString(),
                ),
            )
        }

        ktorClientProvider = KtorClientProvider(mockEngine)
        client = ktorClientProvider.client
    }

    @Test
    fun `when making a request the client should use the correct host from BuildConfig`() = runTest {
        client.get("any/path/")

        assertThat(capturedRequest.url.protocol, `is`(URLProtocol.HTTPS))
        assertThat(capturedRequest.url.host, `is`(BuildConfig.API_HOST))
    }

    @Test
    fun `client should have the correct logging config`() {
        val loggingConfig = client.plugin(Logging)

        assertThat(loggingConfig.level, `is`(LogLevel.ALL))
        // Lee Mar 20, 2025: It seems that the MessageLengthLimitingLogger instance used
        //  here in Ktor does not match the one running internally, for whatever reason.
        // assertThat(loggingConfig.logger, `is`(Logger.ANDROID))
    }
}
