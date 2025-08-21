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
    @SerializedName("requestPackageName") val requestPackageName: String?, //com.package.name
    @SerializedName("nonce") val nonce: String?, //aGVsbG8gd29scmQgdGhlcmU
    @SerializedName("timestampMillis") val timestampMillis: String?, //1675655009345
)

data class AppIntegrity(
    @SerializedName("appRecognitionVerdict") val appRecognitionVerdict: String?, // PLAY_RECOGNIZED, UNRECOGNIZED_VERSION of UNEVALUATED.
    @SerializedName("packageName") val packageName: String?, //com.package.name
    @SerializedName("certificateSha256Digest") val certificateSha256Digest: List<String?>, //["6a6a1474b5cbbb2b1aa57e0bc3"]
    @SerializedName("versionCode") val versionCode: String?, //2
)

data class DeviceIntegrity(
    //[
    //    "MEETS_BASIC_INTEGRITY",
    //    "MEETS_DEVICE_INTEGRITY",
    //    "MEETS_STRONG_INTEGRITY"
    //  ]
    @SerializedName("deviceRecognitionVerdict") val deviceRecognitionVerdict: List<String?>,
    @SerializedName("recentDeviceActivity") val recentDeviceActivity: RecentDeviceActivity?, // LEVEL_1, LEVEL_2
    @SerializedName("deviceAttributes") val deviceAttributes: DeviceAttributes? //33
) {
    data class RecentDeviceActivity(
        @SerializedName("deviceActivityLevel") val deviceActivityLevel: String?
    )
    data class DeviceAttributes(
        @SerializedName("sdkVersion") val sdkVersion: String?
    )
}

data class AccountDetails(
    @SerializedName("appLicensingVerdict") val appLicensingVerdict: String? //LICENSED, UNLICENSED of UNEVALUATED
)

data class EnvironmentDetails(
    @SerializedName("playProtectVerdict") val playProtectVerdict: String?, //NO_DATA, POSSIBLE_RISK, MEDIUM_RISK
    @SerializedName("appAccessRiskVerdict") val appAccessRiskVerdict: AppsDetected?
) {
    data class AppsDetected(
        @SerializedName("appsDetected") val appsDetected: List<String?>
    )
}