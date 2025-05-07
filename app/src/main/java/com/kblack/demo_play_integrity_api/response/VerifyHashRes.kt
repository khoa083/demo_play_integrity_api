package com.kblack.demo_play_integrity_api.response

data class Message (
    val tokenPayload: TokenPayload
)

data class TokenPayload (
    val requestDetails: RequestDetails,
    val appIntegrity: AppIntegrity,
    val deviceIntegrity: DeviceIntegrity,
    val accountDetails: AccountDetails,
    val environmentDetails: EnvironmentDetails,
) {
    data class RequestDetails (
        // Application package name this attestation was requested for.
        // Note that this field might be spoofed in the middle of the request.
        val requestPackageName: String,
        // base64-encoded URL-safe no-wrap nonce provided by the developer.
        val nonce: String,
        // The timestamp in milliseconds when the integrity token
        // was requested.
        val timestampMillis: String,
    )

    data class AppIntegrity (
        // PLAY_RECOGNIZED, UNRECOGNIZED_VERSION, or UNEVALUATED.
        val appRecognitionVerdict: String,
        // The package name of the app.
        // This field is populated iff appRecognitionVerdict != UNEVALUATED.
        val packageName: String,
        // The sha256 digest of app certificates (base64-encoded URL-safe).
        // This field is populated iff appRecognitionVerdict != UNEVALUATED.
        val certificateSha256Digest: List<String>,
        // The version of the app.
        // This field is populated iff appRecognitionVerdict != UNEVALUATED.
        val versionCode: String,
    )

    data class DeviceIntegrity (
        // "MEETS_DEVICE_INTEGRITY" is one of several possible values.
        val deviceRecognitionVerdict: List<String>
    )

    data class AccountDetails (
        // This field can be LICENSED, UNLICENSED, or UNEVALUATED.
        val appLicensingVerdict: String
    )

    data class EnvironmentDetails (
        val appAccessRiskVerdict: AppsDetected
    ) {
        data class AppsDetected (
            // This field contains one or more responses, for example the following.
            val appsDetected: List<String>
        )
    }

}