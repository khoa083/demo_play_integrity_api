package com.kblack.demo_play_integrity_api.utils

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.play.core.integrity.IntegrityManagerFactory
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

class Utils {
    companion object {
         fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
            this.observe(owner, Observer {
                it?.let {
                    observer(it)
                }
            })
        }
    }
}