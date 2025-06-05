package com.kblack.demo_play_integrity_api.repository

import android.content.Context
import com.kblack.base.BaseRepository
import com.kblack.demo_play_integrity_api.api.RetrofitClient
import com.kblack.demo_play_integrity_api.model.PIAResponse
import com.kblack.demo_play_integrity_api.request.PIARequest
import kotlinx.coroutines.flow.Flow

class Repository : BaseRepository() {
    fun sendToken(token: String, context: Context): Flow<Result<PIAResponse>> {
        return executeNetworkCall(
            call = { RetrofitClient.apiService.sendToken(PIARequest(token)).body()!! },
            context = context
        )
    }
}