package com.leehendryp.photoalbum.core.domain

internal const val NO_SUCH_ITEM_ERROR = "No such item was found in the local data."
internal const val RETRIEVAL_ERROR = "An error occurred when getting local data."
internal const val INSERTION_ERROR = "An error occurred when saving local data."

internal sealed interface DomainException {
    class ClientException(message: String, cause: Throwable? = null) :
        Exception(message, cause),
        DomainException

    class ServerException(message: String, cause: Throwable? = null) :
        Exception(message, cause),
        DomainException

    class UnknownException(message: String, cause: Throwable? = null) :
        Exception(message, cause),
        DomainException
}
