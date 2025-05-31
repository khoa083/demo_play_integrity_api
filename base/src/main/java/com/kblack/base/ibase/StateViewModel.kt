package com.kblack.project_base.base.ibase

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class StateViewModel : ViewModel() {

    // MutableStateFlow to manage loading state
    private val _loading by lazy { MutableStateFlow(false) }
    // Publicly exposed StateFlow for loading state
    val loading: StateFlow<Boolean> = _loading

    // MutableStateFlow to manage refreshing state
    private val _refreshing by lazy { MutableStateFlow(false) }
    // Publicly exposed StateFlow for refreshing state
    val refreshing: StateFlow<Boolean> = _refreshing

    // MutableStateFlow to manage error events
    private val _errorEvent by lazy { MutableStateFlow<ErrorEvent?>(null) }
    // Publicly exposed StateFlow for error events
    val errorEvent: StateFlow<ErrorEvent?> = _errorEvent

    /**
     * Sets the loading state to true.
     */
    fun showLoading() {
        _loading.value = true
    }

    /**
     * Sets the loading state to false.
     */
    fun hideLoading() {
        _loading.value = false
    }

    /**
     * Sets the refreshing state to true.
     */
    fun showRefreshing() {
        _refreshing.value = true
    }

    /**
     * Sets the refreshing state to false.
     */
    fun hideRefreshing() {
        _refreshing.value = false
    }

    /**
     * A method to be overridden to perform refresh actions.
     */
    open fun doRefresh() {
    }

    /**
     * Handles exceptions and updates the error event state accordingly.
     *
     * @param e The exception to handle.
     */
    open fun onError(e: Exception) {
        hideLoading()
        hideRefreshing()
        when (e) {
            // Case no internet connection
            is UnknownHostException -> {
                _errorEvent.value = ErrorEvent.Network
            }

            is ConnectException -> {
                _errorEvent.value = ErrorEvent.Network
            }
            // Case request time out
            is SocketTimeoutException -> {
                _errorEvent.value = ErrorEvent.Timeout
            }
        }
    }

    /**
     * Resets the error event state to null.
     */
    fun hideError() {
        _errorEvent.value = null
    }

}