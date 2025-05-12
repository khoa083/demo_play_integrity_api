package com.kblack.demo_play_integrity_api

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.SecureRandom

class MainActivityViewModel : ViewModel() {
    protected val _resultTxt = MutableLiveData<String?>()
    val resultTxt: LiveData<String?> = _resultTxt

    fun clearTxt() {
        _resultTxt.postValue(null)
    }

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
        val bytes = ByteArray(length)
        random.nextBytes(bytes)
        return Base64.encodeToString(
            bytes,
            Base64.URL_SAFE or Base64.NO_WRAP
        )
    }

    fun playIntegrityRequest(
        applicationContext: Context
    ) {
        val nonce = getNonce(16)
        try {
            val integrityManager = IntegrityManagerFactory.create(applicationContext)

            val integrityTokenResponse: Task<IntegrityTokenResponse> =
                integrityManager.requestIntegrityToken(
                    IntegrityTokenRequest.builder()
                        .setNonce(nonce)
                        .setCloudProjectNumber(683679993739)
                        .build()
                )
            integrityTokenResponse.addOnSuccessListener { response ->
//                    val integrityToken: String = response.token()
                val baseUrl = "https://be-integrityapi.vercel.app"
                // println(integrityToken)

                try {
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
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val bodyString = response.body?.string()
                            Log.d(TAG, bodyString?:"Success")
                            _resultTxt.postValue(if (response.isSuccessful) "$bodyString" else "Request failed")
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
}