package com.kblack.project_base.base.ibase

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface IBaseViewModel : DefaultLifecycleObserver, LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }

    override fun onCreate(owner: LifecycleOwner) {
        TODO("Not yet implemented")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        TODO("Not yet implemented")
    }

    override fun onStart(owner: LifecycleOwner) {
        TODO("Not yet implemented")
    }

    override fun onStop(owner: LifecycleOwner) {
        TODO("Not yet implemented")
    }

    override fun onResume(owner: LifecycleOwner) {
        TODO("Not yet implemented")
    }

    override fun onPause(owner: LifecycleOwner) {
        TODO("Not yet implemented")
    }
}