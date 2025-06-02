package com.kblack.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VB: ViewDataBinding, VM: BaseViewModel> : AppCompatActivity() {
    private var _binding: VB? = null
    protected val activityBinding get() = _binding!!

    abstract val viewModel: VM
    abstract val layoutId: Int
    abstract val idContainerView: Int

    private fun setSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(idContainerView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        activityBinding.lifecycleOwner = this
        setupView(activityBinding)
        setSystemBars()
    }

    abstract fun setupView(activityBinding: VB)

    abstract fun showView(isShow: Boolean)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}