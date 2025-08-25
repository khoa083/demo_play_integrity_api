package com.kblack.demo_play_integrity_api

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.perf.metrics.Trace
import com.kblack.base.BaseRepository
import com.kblack.base.BaseViewModel
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.firebase.AnalyticsEvents
import com.kblack.demo_play_integrity_api.firebase.FirebaseManager
import com.kblack.demo_play_integrity_api.model.PIAResponse
import com.kblack.demo_play_integrity_api.repository.Repository
import com.kblack.demo_play_integrity_api.utils.ErrorHandler
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.getRequestHashLocal
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.sendTokenToLocal
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val repository: Repository,
    private val firebaseManager: FirebaseManager,
    private val errorHandler: ErrorHandler
) : BaseViewModel() {

    private val _result = MutableLiveData<DataResult<PIAResponse>?>()
    val result: LiveData<DataResult<PIAResponse>?> = _result

    private val _resultRAW = MutableLiveData<DataResult<Any>?>()
    val resultRAW: LiveData<DataResult<Any>?> = _resultRAW

    private var integrityTokenProvider: StandardIntegrityTokenProvider? = null
    private var currentTrace: Trace? = null

    fun prepareIntegrityTokenProvider(applicationContext: Context) {
        val trace = firebaseManager.startTrace("prepare_integrity_token")

        try {
            val standardIntegrityManager: StandardIntegrityManager =
                IntegrityManagerFactory.createStandard(applicationContext)

            standardIntegrityManager.prepareIntegrityToken(
                PrepareIntegrityTokenRequest.builder()
                    .setCloudProjectNumber(BuildConfig.CLOUD_PROJECT_NUMBER.toLong())
                    .build()
            )
                .addOnSuccessListener { tokenProvider ->
                    integrityTokenProvider = tokenProvider
                    trace.stop()

                    firebaseManager.logEvent("integrity_token_provider_prepared")
                    firebaseManager.log("Integrity token provider prepared successfully")
                }
                .addOnFailureListener { exception ->
                    trace.stop()
                    handleError(exception, "prepare_token_provider")
                }
        } catch (e: Exception) {
            trace.stop()
            handleError(e, "prepare_token_provider")
        }
    }

    fun playIntegrityRequest(applicationContext: Context) {
        val startTime = System.currentTimeMillis()
        currentTrace = firebaseManager.startTrace("play_integrity_standard_request")

        // Log analytics event
        firebaseManager.logEvent(AnalyticsEvents.PLAY_INTEGRITY_REQUEST, Bundle().apply {
            putString(AnalyticsEvents.PARAM_REQUEST_TYPE, AnalyticsEvents.REQUEST_TYPE_STANDARD)
        })

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
                    val responseTime = System.currentTimeMillis() - startTime
                    currentTrace?.stop()

                    // Log success analytics
                    firebaseManager.logEvent(AnalyticsEvents.PLAY_INTEGRITY_SUCCESS, Bundle().apply {
                        putString(AnalyticsEvents.PARAM_REQUEST_TYPE, AnalyticsEvents.REQUEST_TYPE_STANDARD)
                        putLong(AnalyticsEvents.PARAM_RESPONSE_TIME, responseTime)
                        putInt(AnalyticsEvents.PARAM_TOKEN_LENGTH, response?.token()?.length ?: 0)
                    })
                    Log.d("addOnSuccessListener","Integrity token received: ${response?.token().toString()}")
                    sendTokenToServer(response?.token().toString(), applicationContext)
                }
                ?.addOnFailureListener { exception ->
                    currentTrace?.stop()
                    handlePlayIntegrityError(exception, AnalyticsEvents.REQUEST_TYPE_STANDARD)
                }

        } catch (e: Exception) {
            currentTrace?.stop()
            handlePlayIntegrityError(e, AnalyticsEvents.REQUEST_TYPE_STANDARD)
        }
    }

    fun playIntegrityRequestForLocal(applicationContext: Context) {
        val startTime = System.currentTimeMillis()
        _resultRAW.postValue(DataResult.loading())
        currentTrace = firebaseManager.startTrace("play_integrity_local_request")

        // Log analytics event
        firebaseManager.logEvent(AnalyticsEvents.PLAY_INTEGRITY_LOCAL_REQUEST, Bundle().apply {
            putString(AnalyticsEvents.PARAM_REQUEST_TYPE, AnalyticsEvents.REQUEST_TYPE_LOCAL)
        })

        val nonce = getRequestHashLocal()
        try {
            val integrityManager = IntegrityManagerFactory.create(applicationContext)
            val integrityTokenResponse: Task<IntegrityTokenResponse> =
                integrityManager.requestIntegrityToken(
                    IntegrityTokenRequest.builder()
                        .setNonce(nonce)
                        .build()
                )
            integrityTokenResponse.addOnSuccessListener { response ->
                val responseTime = System.currentTimeMillis() - startTime
                currentTrace?.stop()

                // Log success analytics
                firebaseManager.logEvent(AnalyticsEvents.PLAY_INTEGRITY_LOCAL_SUCCESS, Bundle().apply {
                    putString(AnalyticsEvents.PARAM_REQUEST_TYPE, AnalyticsEvents.REQUEST_TYPE_LOCAL)
                    putLong(AnalyticsEvents.PARAM_RESPONSE_TIME, responseTime)
                    putInt(AnalyticsEvents.PARAM_TOKEN_LENGTH, response.token().length)
                })

                sendTokenToLocal(response.token(), _resultRAW, applicationContext)
            }.addOnFailureListener { e ->
                currentTrace?.stop()
                handlePlayIntegrityError(e, AnalyticsEvents.REQUEST_TYPE_LOCAL)
            }
        } catch (e: Exception) {
            currentTrace?.stop()
            handlePlayIntegrityError(e, AnalyticsEvents.REQUEST_TYPE_LOCAL)
        }
    }

    private fun handleError(exception: Exception, context: String) {
        errorHandler.handleGenericError(exception, context)
        _resultRAW.postValue(DataResult.error("Error in $context: ${exception.message}"))
    }

    private fun handlePlayIntegrityError(exception: Exception, requestType: String) {
        errorHandler.handlePlayIntegrityError(
            exception,
            requestType,
            mapOf("nonce_length" to getRequestHashLocal().length.toString())
        )
        _resultRAW.postValue(DataResult.error("Error in Play Integrity request: ${exception.message}"))
    }

    private fun sendTokenToServer(token: String, context: Context) {
        val trace = firebaseManager.startTrace("send_token_to_server")

        viewModelScope.launch {
            try {
                repository.sendTokenRaw(token, context).collect { result ->
                    when (result) {
                        is BaseRepository.Result.Loading -> _resultRAW.postValue(DataResult.loading())
                        is BaseRepository.Result.Success -> {
                            trace.stop()
                            _resultRAW.postValue(DataResult.success(result.data))

                            firebaseManager.logEvent("token_sent_successfully", Bundle().apply {
                                putInt("response_size", result.data.toString().length)
                            })
                        }
                        is BaseRepository.Result.Error -> {
                            trace.stop()
                            errorHandler.handleGenericError(
                                result.exception,
                                "send_token_to_server",
                                mapOf("token_length" to token.length.toString())
                            )
                            _resultRAW.postValue(DataResult.error(result.exception.message))
                        }
                    }
                }
            } catch (e: Exception) {
                trace.stop()
                errorHandler.handleGenericError(e, "send_token_to_server")
                _resultRAW.postValue(DataResult.error("Exception: ${e.message}"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentTrace?.stop()
    }
}