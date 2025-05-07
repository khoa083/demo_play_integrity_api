package com.kblack.demo_play_integrity_api

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kblack.demo_play_integrity_api.databinding.ActivityMainBinding
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.playIntegrityRequest

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val activityBinding get() = _binding!!

    private val _checkIntegrityTokenResult = MutableLiveData<String>()
    val checkIntegrityTokenResult: LiveData<String> = _checkIntegrityTokenResult


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        activityBinding.apply {
            btnVerify.setOnClickListener {
                playIntegrityRequest(applicationContext, _checkIntegrityTokenResult)
            }
            txtResult.text = checkIntegrityTokenResult.value
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}