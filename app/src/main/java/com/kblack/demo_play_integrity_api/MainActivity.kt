package com.kblack.demo_play_integrity_api

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.kblack.base.BaseActivity
import com.kblack.base.extensions.clickWithTrigger
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.databinding.ActivityMainBinding
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.observeNonNull
import com.kblack.base.extensions.toast
import com.google.gson.Gson
import com.kblack.demo_play_integrity_api.utils.RawJson.formatJsonWithColors

class MainActivity() : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val viewModel: MainActivityViewModel by viewModels()
    override val layoutId: Int = R.layout.activity_main
    override val idContainerView: Int = R.id.main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.prepareIntegrityTokenProvider(applicationContext)
    }

    override fun setupView(activityBinding: ActivityMainBinding) {
        activityBinding.apply {
            btnVerify.clickWithTrigger {
                viewModel.playIntegrityRequest(applicationContext)

            }
            btnVerifyLocal.apply {
                clickWithTrigger {
                    viewModel.playIntegrityRequestForLocal(applicationContext)
                }
            }

            observeData(this@apply)
        }
    }

    private fun observeData(activityBinding: ActivityMainBinding) {
        viewModel.resultRAW.observeNonNull(this@MainActivity) { dataResult ->
            when (dataResult?.status) {
                DataResult.Status.LOADING -> {
                    activityBinding.txtResult.text = ""
                    activityBinding.ldm3.visibility = View.VISIBLE
                }

                DataResult.Status.SUCCESS -> {
//                    activityBinding.txtResult.text = dataResult.data?.toString() ?: "No data"
                    val jsonString = Gson().toJson(dataResult.data)
                    activityBinding.txtResult.text = formatJsonWithColors(jsonString)
                    activityBinding.ldm3.visibility = View.GONE
//                    if (BuildConfig.DEBUG) {
//                        Toast.makeText(this, "$jsonString", Toast.LENGTH_LONG).show()
//                    }
                    // if use toString() method, it will not format the json string
                }

                DataResult.Status.ERROR -> {
                    activityBinding.txtResult.text = dataResult.message ?: "Unknown error"
                    activityBinding.ldm3.visibility = View.GONE
                }

                null -> {
                    activityBinding.ldm3.visibility = View.GONE
                    this@MainActivity.toast("DataResult is null")
                }
            }
        }
    }

    private fun setUpFirebase() {

    }

    override fun showView(isShow: Boolean) {
        TODO("Not yet implemented")
    }
}
