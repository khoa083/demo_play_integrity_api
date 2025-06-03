package com.kblack.demo_play_integrity_api.model

import com.google.gson.annotations.SerializedName

data class PIAFrame<T>(
    val status: String,
    val verdict: Verdict<T>,
)

data class Verdict<T>(
    @SerializedName("tokenPayloadExternal") val tokenPayloadExternal: T
)