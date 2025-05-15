package com.leehendryp.codechallenge.core.domain

internal const val RETRIEVAL_ERROR = "An error occurred when getting local data."
internal const val INSERTION_ERROR = "An error occurred when saving local data."

internal sealed interface CodeChallengeException {
    class ClientException(message: String, cause: Throwable? = null) :
        Exception(message, cause),
        CodeChallengeException

    class ServerException(message: String, cause: Throwable? = null) :
        Exception(message, cause),
        CodeChallengeException

    class UnknownException(message: String, cause: Throwable? = null) :
        Exception(message, cause),
        CodeChallengeException
}
