package com.leehendryp.codechallenge.core.domain

internal class NetworkException(message: String, cause: Throwable) : Exception(message, cause)
internal class ClientException(message: String) : Exception(message, null)
internal class ServerException(message: String) : Exception(message, null)
internal class UnknownException(message: String, cause: Throwable?) : Exception(message, cause)
