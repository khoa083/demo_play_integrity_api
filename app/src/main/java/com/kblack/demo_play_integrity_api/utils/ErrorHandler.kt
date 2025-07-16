package com.kblack.demo_play_integrity_api.utils

import android.os.Bundle
import com.kblack.demo_play_integrity_api.firebase.AnalyticsEvents
import com.kblack.demo_play_integrity_api.firebase.FirebaseManager

class ErrorHandler(
    private val firebaseManager: FirebaseManager
) {

    fun handlePlayIntegrityError(
        error: Throwable,
        requestType: String,
        additionalInfo: Map<String, String> = emptyMap()
    ) {
        // Log to Crashlytics
        firebaseManager.log("Play Integrity API Error - Type: $requestType")
        additionalInfo.forEach { (key, value) ->
            firebaseManager.setCustomKey(key, value)
        }
        firebaseManager.setCustomKey("request_type", requestType)
        firebaseManager.recordException(error)

        // Log to Analytics
        val bundle = Bundle().apply {
            putString(AnalyticsEvents.PARAM_REQUEST_TYPE, requestType)
            putString(AnalyticsEvents.PARAM_ERROR_MESSAGE, error.message ?: "Unknown error")
            additionalInfo.forEach { (key, value) ->
                putString(key, value)
            }
        }
        firebaseManager.logEvent(AnalyticsEvents.PLAY_INTEGRITY_ERROR, bundle)
    }

    fun handleGenericError(
        error: Throwable,
        context: String,
        additionalInfo: Map<String, String> = emptyMap()
    ) {
        firebaseManager.log("Error in $context: ${error.message}")
        additionalInfo.forEach { (key, value) ->
            firebaseManager.setCustomKey(key, value)
        }
        firebaseManager.recordException(error)
    }
}