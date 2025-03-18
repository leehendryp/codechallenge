package com.leehendryp.codechallenge.core.data

import com.leehendryp.codechallenge.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TIME_OUT = 10_000

val ktorClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            },
        )
    }

    engine {
        connectTimeout = TIME_OUT
        socketTimeout = TIME_OUT
    }

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = BuildConfig.API_HOST
        }
    }

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
}
