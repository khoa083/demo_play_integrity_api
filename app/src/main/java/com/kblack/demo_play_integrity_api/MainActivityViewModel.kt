package com.kblack.demo_play_integrity_api

import android.content.Context
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import com.kblack.base.BaseRepository
import com.kblack.base.BaseViewModel
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.model.PIAResponse
import com.kblack.demo_play_integrity_api.repository.Repository
import com.kblack.demo_play_integrity_api.utils.Constant
import kotlinx.coroutines.launch
import java.security.SecureRandom

class MainActivityViewModel(
    private val repository: Repository = Repository()
) : BaseViewModel() {

    private val _result = MutableLiveData<DataResult<PIAResponse>?>()
    val result: LiveData<DataResult<PIAResponse>?> = _result

    fun clearResult() {
        _result.value = null
    }

    private fun getNonce(length: Int = 16): String {
        val random = SecureRandom()
        val bytes = ByteArray(length)
        random.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP)
    }

    fun playIntegrityRequest(applicationContext: Context) {
        val nonce = getNonce(16)
        try {
            val integrityManager = IntegrityManagerFactory.create(applicationContext)
            val integrityTokenResponse: Task<IntegrityTokenResponse> =
                integrityManager.requestIntegrityToken(
                    IntegrityTokenRequest.builder()
                        .setNonce(nonce)
                        .setCloudProjectNumber(Constant.CLOUD_PROJECT_NUMBER)
                        .build()
                )
            integrityTokenResponse.addOnSuccessListener { response ->
                sendTokenToServer(response.token(), applicationContext)
            }.addOnFailureListener { e ->
                _result.postValue(DataResult.error("API Error: ${e.message}"))
            }
        } catch (e: Exception) {
            _result.postValue(DataResult.error("Exception: ${e.message}"))
        }
    }

    //------------------
    private fun sendTokenToServer(token: String, context: Context) {
        viewModelScope.launch {
            repository.sendToken(token, context).collect { result ->
                when (result) {
                    is BaseRepository.Result.Loading -> _result.postValue(DataResult.loading())
                    is BaseRepository.Result.Success -> _result.postValue(DataResult.success(result.data))
                    is BaseRepository.Result.Error -> _result.postValue(DataResult.error(result.exception.message))
                }
            }
        }
    }
}