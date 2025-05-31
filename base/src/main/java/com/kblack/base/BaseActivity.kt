package com.kblack.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VB: ViewDataBinding, VM: BaseViewModel> : AppCompatActivity() {
    private var _binding: VB? = null
    protected val activityBinding get() = _binding!!
    protected abstract val mViewModel: VM
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        activityBinding.lifecycleOwner = this
        setupView(activityBinding)
    }

    abstract fun setupView(activityBinding: VB)

    abstract fun showView(isShow: Boolean)

    abstract fun setStatusBar()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}