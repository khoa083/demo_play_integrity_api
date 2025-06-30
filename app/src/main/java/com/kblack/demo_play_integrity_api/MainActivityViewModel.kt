package com.kblack.demo_play_integrity_api

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import com.google.android.play.core.integrity.StandardIntegrityManager
import com.google.android.play.core.integrity.StandardIntegrityManager.PrepareIntegrityTokenRequest
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityToken
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenProvider
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenRequest
import com.kblack.base.BaseRepository
import com.kblack.base.BaseViewModel
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.model.PIAResponse
import com.kblack.demo_play_integrity_api.repository.Repository
import com.kblack.demo_play_integrity_api.utils.Constant.CLOUD_PROJECT_NUMBER
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.getRequestHashLocal
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.sendTokenToLocal
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val repository: Repository = Repository()
) : BaseViewModel() {

    private val _result = MutableLiveData<DataResult<PIAResponse>?>()
    val result: LiveData<DataResult<PIAResponse>?> = _result

    private val _resultRAW = MutableLiveData<DataResult<Any>?>()
    val resultRAW: LiveData<DataResult<Any>?> = _resultRAW

    private var integrityTokenProvider: StandardIntegrityTokenProvider? = null

    fun prepareIntegrityTokenProvider(applicationContext: Context) {
        val standardIntegrityManager: StandardIntegrityManager =
            IntegrityManagerFactory.createStandard(applicationContext)

        standardIntegrityManager.prepareIntegrityToken(
            PrepareIntegrityTokenRequest.builder()
                .setCloudProjectNumber(CLOUD_PROJECT_NUMBER)
                .build()
        )
            .addOnSuccessListener { tokenProvider ->
                integrityTokenProvider = tokenProvider
            }
            .addOnFailureListener { exception -> handleError(exception) };
    }

    fun playIntegrityRequest(applicationContext: Context) {
        val requestHash = getRequestHashLocal()
        try {
            val integrityTokenResponse: Task<StandardIntegrityToken?>? =
                integrityTokenProvider?.request(
                    StandardIntegrityTokenRequest.builder()
                        .setRequestHash(requestHash)
                        .build()
                )
            integrityTokenResponse
                ?.addOnSuccessListener { response ->
                    sendTokenToServer(
                        response?.token().toString(),
                        applicationContext
                    )
                }
                ?.addOnFailureListener { exception -> handleError(exception) }

        } catch (e: Exception) {
            _resultRAW.postValue(DataResult.error("Exception: ${e.message}"))
        }
    }

    fun clearResult() {
        _resultRAW.value = null
        _result.value = null
    }

    private fun sendTokenToServer(token: String, context: Context) {
        viewModelScope.launch {
//            repository.sendToken(token, context).collect { result ->
//                when (result) {
//                    is BaseRepository.Result.Loading -> _result.postValue(DataResult.loading())
//                    is BaseRepository.Result.Success -> _result.postValue(DataResult.success(result.data))
//                    is BaseRepository.Result.Error -> _result.postValue(DataResult.error(result.exception.message))
//                }
//            }
            repository.sendTokenRaw(token, context).collect { result ->
                when (result) {
                    is BaseRepository.Result.Loading -> _resultRAW.postValue(DataResult.loading())
                    is BaseRepository.Result.Success -> _resultRAW.postValue(DataResult.success(result.data))
                    is BaseRepository.Result.Error -> _resultRAW.postValue(DataResult.error(result.exception.message))
                }
            }
        }
    }

    private fun handleError(exception: Exception) {
        _resultRAW.postValue(DataResult.error("Error preparing integrity token provider: ${exception.message}"))
        exception.printStackTrace()
//        integrityTokenProvider = null  // Reset the provider on error
    }

    fun playIntegrityRequestForLocal(applicationContext: Context) {
        val nonce = getRequestHashLocal()
        try {
            val integrityManager = IntegrityManagerFactory.create(applicationContext)
            // TODO: Local nothing to do with cloud project number
            val integrityTokenResponse: Task<IntegrityTokenResponse> =
                integrityManager.requestIntegrityToken(
                    IntegrityTokenRequest.builder()
                        .setNonce(nonce)
//                        .setCloudProjectNumber(CLOUD_PROJECT_NUMBER)
                        .build()
                )
            integrityTokenResponse.addOnSuccessListener { response ->
                sendTokenToLocal(response.token(), _resultRAW, applicationContext)
            }.addOnFailureListener { e ->
                handleError(e)
            }
        } catch (e: Exception) {
            _resultRAW.postValue(DataResult.error("Exception: ${e.message}"))
        }
    }
}