package com.kblack.demo_play_integrity_api.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.android.play.core.integrity.IntegrityManagerFactory
import okio.ByteString
import okio.ByteString.Companion.encode
import kotlin.math.floor
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse

class Utils {
    companion object {
        /**
         * generates a nonce locally
         */
        private fun getNonce(length: Int): ByteString {
            var nonce = ""
            val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            for (i in 0 until length) {
                nonce += allowed[floor(Math.random() * allowed.length).toInt()].toString()
            }
            return nonce.encode()
        }

        fun playIntegrityRequest(
            applicationContext: Context,
        ) {
            val nonce: String = getNonce(14).toString()
            try {
                // Create an instance of a manager.
                val integrityManager = IntegrityManagerFactory.create(applicationContext)

                // Request the integrity token by providing a nonce.
                val integrityTokenResponse: Task<IntegrityTokenResponse> =
                    integrityManager.requestIntegrityToken(
                        IntegrityTokenRequest.builder()
                            .setNonce(nonce)
                            // .setCloudProjectNumber(757430732184) // hardcoded for now
                            .build()
                    )

                // do play integrity api call
                integrityTokenResponse.addOnSuccessListener { response ->
                    run {
                        // get token
                        val integrityToken: String = response.token()
                        // println(integrityToken)

                        // show received token in UI
                        // playIntegrityResult.value = ResponseType.SuccessSimple(integrityToken)

                        // decode and verify
                        try {
                            decodeAndVerify(verifyType, integrityToken, nonceGeneration, url)
                        } catch (e: JoseException) {
                            Log.d(TAG, "can't decode Play Integrity response")
                            e.printStackTrace()
                            playIntegrityResult.value =
                                ResponseType.Failure(Throwable("can't decode Play Integrity response"))
                            return@run
                        }
                    }
                }.addOnFailureListener { e ->
                    Log.d(TAG, "API Error, see Android UI for error message")
                    playIntegrityResult.value = ResponseType.Failure(e)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}