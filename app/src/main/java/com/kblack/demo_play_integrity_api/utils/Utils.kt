package com.kblack.demo_play_integrity_api.utils

import android.content.Context
import android.util.Base64
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.BuildConfig
import com.kblack.demo_play_integrity_api.model.PIAResponse
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwx.JsonWebStructure
import java.security.KeyFactory
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.X509EncodedKeySpec
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class Utils {
    companion object {
        fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
            this.observe(owner, Observer {
                it?.let {
                    observer(it)
                }
            })
        }
        /**
         * Generates a random nonce of the specified length.
         * The nonce is encoded in Base64 URL-safe format without padding.
         *
         * @param length The length of the nonce to generate. Default is 16 bytes.
         * @return A Base64 URL-safe encoded nonce string.
         */
        fun getRequestHashLocal(length: Int = 16): String {
            val random = SecureRandom()
            val bytes = ByteArray(length)
            random.nextBytes(bytes)
            return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP)
        }


        /**
         * Sends the token to local for testing purposes.
         * This function is used to decrypt and verify the integrity token.
         *
         * @param token The integrity token to be processed.
         * @param _resultRAW MutableLiveData to post the result of the operation.
         */

        // TODO: Only on build internal testing can test
        fun sendTokenToLocal(token: String, _resultRAW: MutableLiveData<DataResult<Any>?>, applicationContext: Context) {
            val base64OfEncodedDecryptionKey = BuildConfig.base64_of_encoded_decryption_key
            val base64OfEncodedVerificationKey = BuildConfig.base64_of_encoded_verification_key

            // base64OfEncodedDecryptionKey is provided through Play Console.
            val decryptionKeyBytes: ByteArray =
                Base64.decode(base64OfEncodedDecryptionKey, Base64.DEFAULT)

            // Deserialized encryption (symmetric) key.
            val decryptionKey: SecretKey = SecretKeySpec(
                decryptionKeyBytes,
                /* offset= */ 0,
                decryptionKeyBytes.size,
                "AES"
            )

            // base64OfEncodedVerificationKey is provided through Play Console.
            val encodedVerificationKey: ByteArray =
                Base64.decode(base64OfEncodedVerificationKey, Base64.DEFAULT)

            // Deserialized verification (public) key.
            val verificationKey: PublicKey = KeyFactory.getInstance("EC")
                .generatePublic(X509EncodedKeySpec(encodedVerificationKey))

            val jwe: JsonWebEncryption =
                JsonWebStructure.fromCompactSerialization(token) as JsonWebEncryption
            jwe.setKey(decryptionKey)

            // This also decrypts the JWE token.
            val compactJws: String = jwe.payload

            val jws: JsonWebSignature =
                JsonWebStructure.fromCompactSerialization(compactJws) as JsonWebSignature
            jws.setKey(verificationKey)

            // This also verifies the signature.
            val payload: String = jws.getPayload()

            val gson = Gson()
            val piaResponse: PIAResponse = gson.fromJson(payload, PIAResponse::class.java)

            _resultRAW.postValue(piaResponse.let {
                DataResult.success(it)
            })
        }

    }
}