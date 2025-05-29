package com.kblack.demo_play_integrity_api.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

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