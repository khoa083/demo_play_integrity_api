package com.kblack.demo_play_integrity_api

import androidx.activity.viewModels
import com.kblack.base.BaseActivity
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.databinding.ActivityMainBinding
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.observeNonNull

class MainActivity() : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val viewModel: MainActivityViewModel by viewModels()
    override val layoutId: Int = R.layout.activity_main
    override val idContainerView: Int = R.id.main

    override fun setupView(activityBinding: ActivityMainBinding) {
        activityBinding.apply {
            btnVerify.setOnClickListener {
                viewModel.playIntegrityRequest(applicationContext)
            }
            viewModel.result.observeNonNull(this@MainActivity) { dataResult ->
                when (dataResult?.status) {
                    DataResult.Status.LOADING -> {
                        txtResult.text = "Loading..."
                    }
                    DataResult.Status.SUCCESS -> {
                        txtResult.text = dataResult.data?.toString() ?: "No data"
                    }
                    DataResult.Status.ERROR -> {
                        txtResult.text = dataResult.message ?: "Unknown error"
                    }

                    null -> TODO()
                }
                viewModel.clearResult()
            }
        }
    }

    override fun showView(isShow: Boolean) {
        TODO("Not yet implemented")
    }

}