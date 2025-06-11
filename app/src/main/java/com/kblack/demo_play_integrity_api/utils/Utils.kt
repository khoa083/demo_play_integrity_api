package com.kblack.demo_play_integrity_api.utils

import android.util.Base64
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.security.SecureRandom

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
    }
}