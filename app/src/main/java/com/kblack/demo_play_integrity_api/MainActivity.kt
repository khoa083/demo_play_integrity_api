package com.kblack.demo_play_integrity_api

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kblack.demo_play_integrity_api.databinding.ActivityMainBinding
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.observeNonNull

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val activityBinding get() = _binding!!

    private val viewModel: MainActivityViewModel by viewModels()


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
                viewModel.playIntegrityRequest(applicationContext)
            }
            viewModel.resultTxt.observeNonNull(this@MainActivity) {
                txtResult.text = viewModel.resultTxt.value
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}