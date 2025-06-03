package com.kblack.demo_play_integrity_api.repository

import com.kblack.demo_play_integrity_api.api.RetrofitClient
import com.kblack.demo_play_integrity_api.model.PIAFrame
import com.kblack.demo_play_integrity_api.model.PIAResponse
import com.kblack.demo_play_integrity_api.request.PIARequest
import retrofit2.Response

class Repository {
    suspend fun sendToken(token: String): Response<PIAResponse> {
        return RetrofitClient.apiService.sendToken(PIARequest(token))
    }
}