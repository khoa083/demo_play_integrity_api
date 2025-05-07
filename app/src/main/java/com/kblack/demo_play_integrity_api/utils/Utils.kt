package com.kblack.demo_play_integrity_api.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.play.core.integrity.IntegrityManagerFactory
import okio.ByteString
import okio.ByteString.Companion.encode
import kotlin.math.floor
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import okhttp3.Callback
import java.io.IOException
import okhttp3.Response
import java.security.SecureRandom
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Utils {
    companion object {
        private val TAG = "smleenull"
        private val client = OkHttpClient()


        /**
         * generates a nonce locally
         */
//        private fun getNonce(length: Int): ByteString {
//            var nonce = ""
//            val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
//            for (i in 0 until length) {
//                nonce += allowed[floor(Math.random() * allowed.length).toInt()].toString()
//            }
//            return nonce.encode()
//        }
        private fun getNonce(length: Int = 16): String {
            val random = SecureRandom()
            val bytes = ByteArray(length) // Tạo mảng byte ngẫu nhiên
            random.nextBytes(bytes)
            return Base64.encodeToString(
                bytes,
                Base64.URL_SAFE or Base64.NO_WRAP // Base64 web-safe no-wrap
            )
        }

        fun playIntegrityRequest(
            applicationContext: Context,
            _checkIntegrityTokenResult: MutableLiveData<String>,
        ) {
            val nonce = getNonce(16)
            try {
                // Create an instance of a manager.
                val integrityManager = IntegrityManagerFactory.create(applicationContext)

                // Request the integrity token by providing a nonce.
                val integrityTokenResponse: Task<IntegrityTokenResponse> =
                    integrityManager.requestIntegrityToken(
                        IntegrityTokenRequest.builder()
                            .setNonce(nonce)
                            .setCloudProjectNumber(683679993739) // hardcoded for now
                            .build()
                    )

                // do play integrity api call
                integrityTokenResponse.addOnSuccessListener { response ->
//                    val integrityToken: String = response.token()
                    val baseUrl = "https://be-integrityapi.vercel.app"
                    // println(integrityToken)
                    _checkIntegrityTokenResult.postValue(response.token())
                    // show received token in UI
                    // playIntegrityResult.value = ResponseType.SuccessSimple(integrityToken)

                    // decode and verify
                    try {
//                            sendToServer(integrityToken, nonce)
                        // using okhttp3
                        val jsonParams = JSONObject()
                        jsonParams.put("integrityToken", response.token())
                        val jsonData = jsonParams.toString()
                        val requestBody: RequestBody = jsonData.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                        Log.d(TAG, jsonData)

                        val request = Request.Builder()
                            .url("${baseUrl}/verify-integrity")
                            .post(requestBody)
                            .build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                                _checkIntegrityTokenResult.postValue("Request for checking failed")
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val bodyString = response.body?.string()
                                Log.d(TAG, bodyString?:"Success")
                                _checkIntegrityTokenResult.postValue(if (response.isSuccessful) "$bodyString" else "Request failed")
                            }
                        })



                    } catch (e: Exception) {
                        Log.d(TAG, "can't decode Play Integrity response")
                        e.printStackTrace()
                    }
                }.addOnFailureListener { e ->
                    e.printStackTrace()
                    Log.d(TAG, "API Error, see Android UI for error message")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun sendToServer(integrityToken: String, nonce: String) {
//        val executor: Executor = Executors.newSingleThreadExecutor()
//        val handler = Handler(Looper.getMainLooper())
//        executor.execute {
//
//        }

        }
    }
}