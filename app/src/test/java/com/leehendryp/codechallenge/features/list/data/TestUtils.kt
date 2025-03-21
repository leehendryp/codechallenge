package com.leehendryp.codechallenge.features.list.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal const val EXCEPTION_FAILURE = "Exception described in scenario was not thrown."

internal fun loadJsonFromFile(fileName: String): String {
    val classLoader = Thread.currentThread().contextClassLoader
    return classLoader?.getResource(fileName)?.readText()
        ?: throw IllegalArgumentException("File not found: $fileName")
}

internal fun createMockKtorClient(
    responseContent: String,
    responseStatus: HttpStatusCode,
): HttpClient = createMockKtorClient(
    mockEngine = MockEngine(
        config = MockEngineConfig().setResponse(responseContent, responseStatus),
    ),
)

internal fun createMockKtorClient(
    responseStatus: HttpStatusCode,
): HttpClient = createMockKtorClient(
    mockEngine = MockEngine(
        config = MockEngineConfig().setResponse(responseStatus),
    ),
)

internal fun createMockKtorClient(mockEngine: MockEngine) = HttpClient(mockEngine) {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            },
        )
    }
}

private fun MockEngineConfig.setResponse(status: HttpStatusCode): MockEngineConfig = apply {
    addHandler {
        respond(
            status = status,
            content = """[{"$status": "${status.description}"}]""",
            headers = headersOf(
                HttpHeaders.ContentType,
                ContentType.Application.Json.toString(),
            ),
        )
    }
}

private fun MockEngineConfig.setResponse(
    content: String,
    status: HttpStatusCode,
): MockEngineConfig = apply {
    addHandler {
        respond(
            status = status,
            content = content,
            headers = headersOf(
                HttpHeaders.ContentType,
                ContentType.Application.Json.toString(),
            ),
        )
    }
}
