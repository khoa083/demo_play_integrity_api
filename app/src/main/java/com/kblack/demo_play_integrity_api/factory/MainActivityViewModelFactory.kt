package com.kblack.demo_play_integrity_api.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kblack.demo_play_integrity_api.MainActivityViewModel
import com.kblack.demo_play_integrity_api.firebase.FirebaseManager
import com.kblack.demo_play_integrity_api.repository.Repository
import com.kblack.demo_play_integrity_api.utils.ErrorHandler

class MainActivityViewModelFactory(
    private val repository: Repository,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            val firebaseManager = FirebaseManager(context)
            val errorHandler = ErrorHandler(firebaseManager)
            return MainActivityViewModel(repository, firebaseManager, errorHandler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}