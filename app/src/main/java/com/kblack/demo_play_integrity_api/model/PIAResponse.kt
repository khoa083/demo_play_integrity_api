package com.kblack.demo_play_integrity_api.model

import com.google.gson.annotations.SerializedName

data class PIAResponse(
    @SerializedName("requestDetails") val requestDetails: RequestDetails?,
    @SerializedName("appIntegrity") val appIntegrity: AppIntegrity?,
    @SerializedName("deviceIntegrity") val deviceIntegrity: DeviceIntegrity?,
    @SerializedName("accountDetails") val accountDetails: AccountDetails?,
    @SerializedName("environmentDetails") val environmentDetails: EnvironmentDetails?,
)

data class RequestDetails(
    @SerializedName("requestPackageName") val requestPackageName: String?,
    @SerializedName("nonce") val nonce: String?,
    @SerializedName("timestampMillis") val timestampMillis: String?,
)

data class AppIntegrity(
    @SerializedName("appRecognitionVerdict") val appRecognitionVerdict: String?,
    @SerializedName("packageName") val packageName: String?,
    @SerializedName("certificateSha256Digest") val certificateSha256Digest: List<String?>,
    @SerializedName("versionCode") val versionCode: String?,
)

data class DeviceIntegrity(
    @SerializedName("deviceRecognitionVerdict") val deviceRecognitionVerdict: List<String?>
)

data class AccountDetails(
    @SerializedName("appLicensingVerdict") val appLicensingVerdict: String?
)

data class EnvironmentDetails(
    @SerializedName("appAccessRiskVerdict") val appAccessRiskVerdict: AppsDetected?
) {
    data class AppsDetected(
        @SerializedName("appsDetected") val appsDetected: List<String?>
    )
}