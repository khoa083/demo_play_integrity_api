package com.kblack.demo_play_integrity_api.api

import com.kblack.demo_play_integrity_api.model.PIAFrame
import com.kblack.demo_play_integrity_api.model.PIAResponse
import com.kblack.demo_play_integrity_api.request.PIARequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServices {
    @POST("/verify-integrity")
    suspend fun sendToken(@Body integrityToken: PIARequest): Response<PIAResponse>
}