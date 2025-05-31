package com.kblack.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kblack.project_base.base.ibase.IBaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel(), IBaseViewModel {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState


    // exception handler for coroutine
    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                onError(throwable)
            }
        }
    }
    /**
     * handle throwable when load fail
     */
    protected fun toErrorType(throwable: Throwable): ErrorType {
        return when (throwable) {
            // case no internet connection
            is UnknownHostException -> {
                ErrorType.NoInternetConnection
            }

            is ConnectException -> {
                ErrorType.NoInternetConnection
            }
            // case request time out
            is SocketTimeoutException -> {
                ErrorType.ConnectTimeout
            }

            else -> {ErrorType.ConnectTimeout}
        }
    }

    protected open fun onError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorType = toErrorType(throwable)
            )
        }
    }

    open fun hideError() {
        _uiState.update {
            it.copy(errorType = null)
        }
    }

    fun showLoading() {
        _uiState.update {
            it.copy(isLoading = true, errorType = null)
        }
    }

    fun hideLoading() {
        _uiState.update {
            it.copy(isLoading = false)
        }
    }

    fun isLoading() = uiState.value.isLoading

    override fun onCleared() {
        super.onCleared()
        TODO("Not yet implemented")
    }
}