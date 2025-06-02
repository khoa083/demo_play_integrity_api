package com.kblack.demo_play_integrity_api

import androidx.activity.viewModels
import com.kblack.base.BaseActivity
import com.kblack.demo_play_integrity_api.databinding.ActivityMainBinding
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.observeNonNull

class MainActivity() : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val viewModel: MainActivityViewModel by viewModels()
    override val layoutId: Int = R.layout.activity_main
    override val idView: Int = R.id.main

    override fun onResume() {
        super.onResume()
        activityBinding.apply {
            btnVerify.setOnClickListener {
                viewModel.playIntegrityRequest(applicationContext)
            }
            viewModel.resultTxt.observeNonNull(this@MainActivity) {
                txtResult.text = viewModel.resultTxt.value
                viewModel.clearTxt()
            }
        }
    }

    override fun setupView(activityBinding: ActivityMainBinding) {
        TODO("Not yet implemented")
    }

    override fun showView(isShow: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setStatusBar() {
        TODO("Not yet implemented")
    }

}