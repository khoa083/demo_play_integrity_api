package com.kblack.demo_play_integrity_api.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace

class FirebaseManager(
    private val context: Context
) {
    private val analytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }
    private val crashlytics: FirebaseCrashlytics by lazy { FirebaseCrashlytics.getInstance() }
    private val performance: FirebasePerformance by lazy { FirebasePerformance.getInstance() }

    // Analytics
    fun logEvent(eventName: String, parameters: Bundle? = null) {
        analytics.logEvent(eventName, parameters)
    }

    fun setUserProperty(name: String, value: String) {
        analytics.setUserProperty(name, value)
    }

    fun setUserId(userId: String) {
        analytics.setUserId(userId)
    }

    // Crashlytics
    fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    fun log(message: String) {
        crashlytics.log(message)
    }

    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCrashlyticsUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    // Performance
    fun startTrace(traceName: String): Trace {
        return performance.newTrace(traceName).apply { start() }
    }

    fun stopTrace(trace: Trace) {
        trace.stop()
    }

    fun createHttpMetric(url: String, httpMethod: String) =
        performance.newHttpMetric(url, httpMethod)
}