package com.kblack.project_base.base.ibase

/**
 * Represents different types of error events that can occur in the application.
 *
 * @property type The type of the error.
 * @property baseException The base exception associated with the error, if any.
 */
@Suppress("unused")
sealed class ErrorEvent(
    val type: ErrorType,
) {
    /**
     * Represents a network error event.
     */
    data object Network : ErrorEvent(type = ErrorType.NETWORK)

    /**
     * Represents a timeout error event.
     */
    data object Timeout : ErrorEvent(type = ErrorType.TIMEOUT)

    /**
     * Represents an unauthorized HTTP error event.
     */
    data object Unauthorized : ErrorEvent(type = ErrorType.HTTP_UNAUTHORIZED)

    /**
     * Represents a force update error event.
     */
    data object ForceUpdate : ErrorEvent(type = ErrorType.FORCE_UPDATE)

}

/**
 * Enum class representing different types of errors.
 */
enum class ErrorType {
    NETWORK,
    TIMEOUT,
    HTTP_UNAUTHORIZED,
    FORCE_UPDATE,
    UNKNOWN
}