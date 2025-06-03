package com.kblack.demo_play_integrity_api.request

import com.google.gson.annotations.SerializedName

data class PIARequest (
    @SerializedName("integrityToken")
    val token: String
)