package com.kblack.demo_play_integrity_api.firebase

object AnalyticsEvents {
    const val PLAY_INTEGRITY_REQUEST = "play_integrity_request"
    const val PLAY_INTEGRITY_SUCCESS = "play_integrity_success"
    const val PLAY_INTEGRITY_ERROR = "play_integrity_error"
    const val PLAY_INTEGRITY_LOCAL_REQUEST = "play_integrity_local_request"
    const val PLAY_INTEGRITY_LOCAL_SUCCESS = "play_integrity_local_success"
    const val PLAY_INTEGRITY_LOCAL_ERROR = "play_integrity_local_error"

    // Parameters
    const val PARAM_REQUEST_TYPE = "request_type"
    const val PARAM_ERROR_MESSAGE = "error_message"
    const val PARAM_TOKEN_LENGTH = "token_length"
    const val PARAM_RESPONSE_TIME = "response_time_ms"

    // Values
    const val REQUEST_TYPE_STANDARD = "standard"
    const val REQUEST_TYPE_LOCAL = "local"

    // Custom events
    const val ERROR = "error"
    const val SUCCESS = "success"
    const val REQUEST = "request"
}